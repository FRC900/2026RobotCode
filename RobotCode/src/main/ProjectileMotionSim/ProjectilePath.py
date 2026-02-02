"""
This script employs a Runge-Kutta 4 antiderivative approximation of the 
nonlinear ordinary differential equations that govern the motion of an
object through a fluid. These differential equations include the change 
in x, y, and z velocities of the object, along with the change in x, y, 
and z positions of the object.

The simulation accounts for the following:
1. Gravity
2. Drag
3. Magnus Force (lift)
4. Rotational drag torque

The simulation assumes the following:
- Gravity acts along the -y direction.
- Only spin about the z-axis is present (i.e., topspin or backspin).

Suppose we are given a target set of coordinates, but want to know the 
x, y, and z velocities and spin needed to move the object to those
coordinates. This is essentially the inverse of what the RK4 simulation
is doing. Since there are no equations relating displacement to velocity
(we have a set of differential equations relating velocity to time which
required RK4 to solve), we cannot analytically derive a closed-form 
inverse. 

Thus, we employ the Levenberg-Marquardt algorithm (a combination of the
Newton-Raphson method and gradient descent) to change the velocity and
spin inputs until we reach a solution. The program is designed to 
allow us to limit the solution velocity and spins based on physical
and angular constraints. 
"""

import numpy as np 
import matplotlib.pyplot as plt


# Runge-Kutta 4 numerical integrator 
class RungeKutta4:
    def __init__(self, dadt, t0, dt, a0):
        # t and a are initialized to the differential equation solution's initial conditions
        
        self.t = t0 # time
        self.a = a0 # rk4 approximation
        self.dt = dt # timestep
        self.dadt = dadt # differential equation

        self.t_list = [self.t] # list of times
        self.a_list = [self.a] # list of RK4-approximations

    # approximate the differential equation's antiderivative
    def rk4(self):
        k1 = self.dt*self.dadt(self.t, self.a)
        k2 = self.dt*self.dadt(self.t+self.dt/2., self.a+k1/2.)
        k3 = self.dt*self.dadt(self.t+self.dt/2., self.a+k2/2.)
        k4 = self.dt*self.dadt(self.t+self.dt, self.a+k3)
        
        self.a = self.a + ((k1+2*k2+2*k3+k4)/6.)
        self.t += self.dt

    # run until the simulation reaches the designated end time 
    def sim(self, timelen=10, stop_on_y_zero=False):
        while self.t <= timelen: # stop RK4 when it reaches specified simulation time
            if stop_on_y_zero: # stop RK4 when y-position<0 (i.e., object hits the ground)
                if self.a[1] < 0:
                    break 
                
            self.rk4()
            self.t_list.append(self.t)
            self.a_list.append(self.a.copy())


class Projectile:
    def __init__(self, radius, mass, I=False, rho=1.195, mu=1.835e-5, g=9.81, spin_coeff=lambda Re: 0.02, drag_coeff=lambda Re: 0.47, lift_coeff=lambda S: 1.5 * S):
        self.radius = radius # object radius, m 
        self.csarea = np.pi * (self.radius**2) # object cross-sectional area, m^2
        self.mass = mass # object mass, kg
        if I: # object moment of inertia, kg*m^2
            self.I = I
        else:
            self.I = 0.4 * self.mass * (self.radius**2) 

        self.omega0 = 0 # object initial angular velocity, rad/s
        self.n_hat = 1 # angular velocity direction unit vector (+1: backspin, -1: topspin, 0: no spin)
        
        self.rho = rho # fluid density, kg/m^3
        self.mu = mu # fluid dynamic viscosity, N*s/m^2
        self.g = g # acceleration due to gravity, m/s^2

        self.spin_coeff = spin_coeff # rotational drag coefficient  
        self.drag_coeff = drag_coeff # coefficient of drag
        self.lift_coeff = lift_coeff # coefficient of lift (Magnus effect)

    # calculate Reynold's number
    def reynolds(self, speed):
        return (self.rho*speed*2*self.radius) / self.mu

    # angular velocity as a function of time
    def omega(self, t, speed):
        if self.omega0 < 1e-8:
            return 0.0
        denom1 = 1 / self.omega0
        denom2 = (t*self.spin_coeff(self.reynolds(speed))*self.rho*(self.radius**5)) / (2*self.I) 
        return self.n_hat / (denom1+denom2)

    # calculate shear parameter of the object 
    def shear(self, t, speed):
        if speed < 1e-8:
            return 0.0
        return (np.abs(self.omega(t, speed))*self.radius) / speed

    # change in x-velocity vs. time    
    def dvxdt(self, t, speed, vx, vy):
        coeff = (-1*self.rho*self.csarea*speed) / (2*self.mass) # coefficient of constants in the general dv_x/dt formula 
        w = self.omega(t, speed)
        return coeff * ((self.drag_coeff(self.reynolds(speed))*vx) + (self.lift_coeff(self.shear(t, speed))*np.sign(w)*vy))

    # change in y-velocity vs. time
    def dvydt(self, t, speed, vx, vy):
        coeff = (self.rho*self.csarea*speed) / (2*self.mass) # coefficient of constants in the general dv_y/dt formula 
        w = self.omega(t, speed)
        return coeff * ((-1*self.drag_coeff(self.reynolds(speed))*vy) + (self.lift_coeff(self.shear(t, speed))*np.sign(w)*vx)) - self.g 

    # change in z-velocity vs. time
    def dvzdt(self, speed, vz):
        coeff = (-1*self.rho) / (2*self.mass) # coefficient of constants in the general dv_z/dt formula 
        return coeff * self.drag_coeff(self.reynolds(speed)) * self.csarea * speed * vz

    # change in velocity vector vs. time
    def dvdt(self, t, a):
        vx, vy, vz = a
        speed = np.linalg.norm(a)

        dvx = self.dvxdt(t, speed, vx, vy)
        dvy = self.dvydt(t, speed, vx, vy)
        dvz = self.dvzdt(speed, vz)

        return np.array([dvx, dvy, dvz], dtype=float)

    # function to simulate velocity and position over time 
    def trajectory(self, vx0, vy0, vz0, omega0=0, n_hat=1, sx0=0, sy0=0, sz0=0, t0=0, dt=0.01, sim_end_time=10, stop_on_y_zero=False):
        # set initial conditions 
        self.omega0 = omega0 # initial angular velocity (rad/s)
        self.n_hat = n_hat # initial angular velocity direction

        v0_vec = np.array([vx0, vy0, vz0], dtype=float) # vector of initial velocities (m/s)
        p0_vec = np.array([sx0, sy0, sz0], dtype=float) # vector of initial positions (m)

        # approximate velocity of object
        vel_approx = RungeKutta4(self.dvdt, t0, dt, v0_vec)
        vel_approx.sim(timelen=sim_end_time)
        v_list = vel_approx.a_list
        vx_list, vy_list, vz_list = map(list, zip(*vel_approx.a_list))

        # change in position vector vs. time
        def dsdt(t, _):
            idx = int(t / dt)
            dsx = v_list[idx][0] 
            dsy = v_list[idx][1]
            dsz = v_list[idx][2]

            return np.array([dsx, dsy, dsz], dtype=float)

        # approximate position of object
        pos_approx = RungeKutta4(dsdt, t0, dt, p0_vec)
        pos_approx.sim(timelen=sim_end_time, stop_on_y_zero=stop_on_y_zero)
        sx_list, sy_list, sz_list = map(list, zip(*pos_approx.a_list))

        return vel_approx.t_list, vx_list, vy_list, vz_list, pos_approx.t_list, sx_list, sy_list, sz_list

    # plot the outputs of the simulation
    def plot_solutions(self, vel_approx_tlist, vx_list, vy_list, vz_list, pos_approx_tlist, sx_list, sy_list, sz_list):
        plt.scatter(vel_approx_tlist, vx_list, s=1, color="red")
        plt.xlabel("Time (s)")
        plt.ylabel("X-Velocity (m/s)")
        plt.title("X-Velocity vs. Time")
        plt.show()

        plt.scatter(vel_approx_tlist, vy_list, s=1, color="blue")
        plt.xlabel("Time (s)")
        plt.ylabel("Y-Velocity (m/s)")
        plt.title("Y-Velocity vs. Time")
        plt.show()

        plt.scatter(vel_approx_tlist, vz_list, s=1, color="green")
        plt.xlabel("Time (s)")
        plt.ylabel("Z-Velocity (m/s)")
        plt.title("Z-Velocity vs. Time")
        plt.show()

        plt.scatter(pos_approx_tlist, sx_list, s=1, color="red")
        plt.xlabel("Time (s)")
        plt.ylabel("X-Position (m)")
        plt.title("X-Position vs. Time")
        plt.show()

        plt.scatter(pos_approx_tlist, sy_list, s=1, color="blue")
        plt.xlabel("Time (s)")
        plt.ylabel("Y-Position (m)")
        plt.title("Y-Position vs. Time")
        plt.show()

        plt.scatter(pos_approx_tlist, sz_list, s=1, color="green")
        plt.xlabel("Time (s)")
        plt.ylabel("Z-Position (m)")
        plt.title("Z-Position vs. Time")
        plt.show()

        fig = plt.figure()
        ax = fig.add_subplot(111, projection='3d')

        t_f = pos_approx_tlist
        scf = ax.scatter(
            sx_list, sy_list, sz_list,
            c=t_f, s=6, cmap='cividis'
        )
        plt.colorbar(scf, ax=ax, label='Time--Full Model (s)')
        ax.plot(sx_list, sy_list, sz_list, linewidth=1, label="Full Trajectory", color="olive")
        ax.set_xlabel('X Position (m)')
        ax.set_ylabel('Y Position (m)')
        ax.set_zlabel('Z Position (m)')
        ax.set_title('3D Trajectory with Time')
        ax.legend()

        plt.show()


# class to solve for vx0, vy0, vz0, and omega0 given a target x, y, z 
class ProjectileSolver:
    def __init__(self, projectile, xt, yt, zt, vx0, vy0, vz0, omega0, ct=0, thetat=0, phit=0, x_scale=0.05, y_scale=0.05, z_scale=0.05, c_scale=0.01, theta_scale=0.1, phi_scale=0.1, n_hat=1, sx0=0, sy0=0, sz0=0, t0=0, dt=0.01, sim_end_time=10, stop_on_y_zero=False, eps=1e-4, lam0=1e-2, tol=np.full(6, 1e-4), lm_iters=20, lam_scaleup=8, lam_scaledown=0.2):
        self.projectile = projectile # projectile object being solved
        self.targets = np.array([xt, yt, zt, ct, thetat, phit]) # target x (m), y (m), z (m), satisfies clearance threshold, satisfies theta range, and satisfies phi range -- clearance: defined if object needs to clear a physical threshold, theta (polar angle down from the +z axis): defined if object can be shot at only a range of thetas, phi (azimuthal angle in the x–y plane, measured from the +x-axis): defined if object can be shot at only a range of phis
        self.scale_array = np.array([x_scale, y_scale, z_scale, c_scale, theta_scale, phi_scale]) # weight of each parameter on residual calculation  

        self.sx0 = sx0 # initial x-position (m)
        self.sy0 = sy0 # initial y-position (m) 
        self.sz0 = sz0 # initial z-position (m)

        self.vx = vx0 # x-velocity (m/s)
        self.vy = vy0 # y-velocity (m/s)
        self.vz = vz0 # z-velocity (m/s)
        self.omega = omega0 # angular velocity (rad/s)
        self.n_hat = n_hat # angular velocity direction unit vector (+1: backspin, -1: topspin, 0: no spin)

        self.t0 = t0 # initial time (s)
        self.dt = dt # timestep (s)
        self.sim_end_time = sim_end_time # simulation duration (s)
        self.stop_on_y_zero = stop_on_y_zero # stop RK4 when y-position=0 (i.e., object hits the ground)
        
        self.eps = eps # Jacobian approximation delta t (s)
        self.lam = lam0 # Levenberg–Marquardt damping parameter
        self.tol = tol # solution accuracy tolerance 
        self.lm_iters = lm_iters # max Levenberg–Marquardt iterations 
        self.lam_scaleup = lam_scaleup # lambda scaleup factor
        self.lam_scaledown = lam_scaledown # lambda scaledown factor
    
    def possible_theta(self, vx, vy, vz):
        # function to check if angle is possible
        # add other params regarding angle bounds
        # continous output
        return 0
    
    def possible_phi(self, vx, vy, vz):
        # function to check if angle is possible
        # add other params regarding angle bounds
        # continous output
        return 0
    
    def clearance(self, sx_list, sy_list, sz_list):
        # function to check if is clear or not (is diameter within the opening)
        # add other params regarding clearance bounds
        # continous output 
        return 0
    
    # simulate shot and return final state 
    def rk4_shot(self):
        vel_approx_tlist, vx_list, vy_list, vz_list, pos_approx_tlist, sx_list, sy_list, sz_list = self.projectile.trajectory(self.vx, self.vy, self.vz, omega0=self.omega, n_hat=self.n_hat, sx0=self.sx0, sy0=self.sy0, sz0=self.sz0, t0=self.t0, dt=self.dt, sim_end_time=self.sim_end_time, stop_on_y_zero=self.stop_on_y_zero)
        xf, yf, zf = sx_list[-1], sy_list[-1], sz_list[-1]
        cf = self.clearance(sx_list, sy_list, sz_list)
        thetaf = self.possible_theta(self.vx, self.vy, self.vz)
        phif = self.possible_phi(self.vx, self.vy, self.vz)
        return np.array([xf, yf, zf, cf, thetaf, phif])
    
    # difference between current final state and desired final state
    def residual(self):
        shot_output = self.rk4_shot()
        return (shot_output-self.targets) / self.scale_array
    
    # matrix of change in residual with respect to change in vx, vy, vz, and omega0
    def jacobian(self):
        r0 = self.residual()
        J = np.zeros((r0.shape[0], 4))
        with_respect_to = ["vx", "vy", "vz", "omega"]

        for i, name in enumerate(with_respect_to):
            setattr(self, name, getattr(self, name)+self.eps)
            r = self.residual()
            J[:, i] = (r-r0) / self.eps
            setattr(self, name, getattr(self, name)-self.eps)

        return J

    # Levenberg–Marquardt algorithm: combination of Newton-Raphson method and gradient descent to minimize the cost function of the residual 
    def levenberg_marquardt(self):
        for _ in range(self.lm_iters):
            # calculate residual of current vx, vy, vz, and omega0
            r = self.residual()
            cost = np.dot(r, r)

            J = self.jacobian()
            H = J.T @ J # Hessian shortcut
            
            diag_H = np.diag(np.diag(H))
            A = H + self.lam * (np.eye(H.shape[0])+diag_H)
            B = -(J.T @ r)

            delta = np.linalg.solve(A, B) # attempt to solve Ax = B, where x is the change in vx, vy, vz, and omega0

            vxi, vyi, vzi, omegai = self.vx, self.vy, self.vz, self.omega # save original inputs

            # update vx, vy, vz, and omega0 with delta
            self.vx += delta[0]
            self.vy += delta[1]
            self.vz += delta[2]
            self.omega += delta[3]

            # calculate new residual 
            r_new = self.residual()
            cost_new = np.dot(r_new, r_new)

            if cost_new < cost: # if change lowers cost
                self.lam *= self.lam_scaledown # scale down lambda, move closer to Newton-Raphson (faster)
                if np.all(np.abs(self.residual()) < self.tol): # check if a solution has been reached
                    return True
            else:
                self.vx, self.vy, self.vz, self.omega = vxi, vyi, vzi, omegai # revert back to original inputs
                self.lam *= self.lam_scaleup # scale up lambda, move closer to gradient descent (more stable)

        return False
