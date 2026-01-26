"""
This script employs a Runge-Kutta 4 antiderivative approximation of the 
nonlinear ordinary differential equations that govern the motion of an
object through a fluid. These differential equations include the change 
in x, y, and z velocities of the object, along with the change in x, y, 
and z positions of the object.

This script is optimized for an FRC "Fuel" object, a ~3-in diameter and 
~0.5 pound foam ball. This object is to be shot into the "Hub" (a 
container) as part of the 2026 FRC game "Rebuilt."

The simulation accounts for the following:
1. Gravity
2. Drag
3. Magnus Force (lift)
4. Rotational drag torque

The simulation assumes the following:
- Gravity acts along the -y direction.
- Only spin about the z-axis is present (i.e., topspin or backspin).
"""

import numpy as np 
import matplotlib.pyplot as plt

# Define Constants 
ball_radius = 0.0762 # ball radius, m 
ball_csarea = np.pi * (ball_radius**2) # ball cross-sectional area, m^2
ball_mass = 0.21500278 # ball mass, kg
omega0 = 0 # ball angular velocity, rad/s
I = 0.4 * ball_mass * (ball_radius**2) # ball moment of inertia, kg*m^2
n_hat = 1 # angular velocity direction unit vector (1 or -1)
rho = 1.195 # fluid density, kg/m^3
mu = 1.835e-5 # fluid dynamic viscosity, N*s/m^2
g = 9.81 # acceleration due to gravity, m/s^2


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
    def sim(self, timelen):
        while self.t <= timelen:
            self.rk4()
            self.t_list.append(self.t)
            self.a_list.append(self.a.copy())


# calculate Reynold's number
def reynolds(speed):
    return (rho*speed*2*ball_radius) / mu

# rotational drag coefficient  
def spin_coeff(Re): # complete
    return 0.02

# angular velocity as a function of time
def omega(t, speed):
    if omega0 < 1e-8:
        return 0.0
    denom1 = 1 / omega0
    denom2 = (t*spin_coeff(reynolds(speed))*rho*(ball_radius**5)) / (2*I) 
    return n_hat / (denom1+denom2)

# calculate shear parameter of the ball 
def shear(t, speed):
    if speed < 1e-8:
        return 0.0
    return (np.abs(omega(t, speed))*ball_radius) / speed

# coefficient of drag
def drag_coeff(Re): # complete
    return 0.47

# coefficient of lift (Magnus effect)
def lift_coeff(S): # complete
    return 1.5 * S

# change in x-velocity vs. time    
def dvxdt(t, speed, vx, vy):
    coeff = (-1*rho*ball_csarea*speed) / (2*ball_mass) # coefficient of constants in the general dv_x/dt formula 
    return coeff * ((drag_coeff(reynolds(speed))*vx) + (lift_coeff(shear(t, speed))*vy))

# change in y-velocity vs. time
def dvydt(t, speed, vx, vy):
    coeff = (rho*ball_csarea*speed) / (2*ball_mass) # coefficient of constants in the general dv_y/dt formula 
    return coeff * ((-1*drag_coeff(reynolds(speed))*vy) + (lift_coeff(shear(t, speed))*vx)) - g 

# change in z-velocity vs. time
def dvzdt(speed, vz):
    coeff = (-1*rho) / (2*ball_mass) # coefficient of constants in the general dv_z/dt formula 
    return coeff * drag_coeff(reynolds(speed)) * ball_csarea * speed * vz

# change in velocity vector vs. time
def dvdt(t, a):
    vx, vy, vz = a
    speed = np.linalg.norm(a)

    dvx = dvxdt(t, speed, vx, vy)
    dvy = dvydt(t, speed, vx, vy)
    dvz = dvzdt(speed, vz)

    return np.array([dvx, dvy, dvz], dtype=float)

# callable function to approximate velocity and position 
def solve(vx0, vy0, vz0, omega0_param=0, n_hat_param=1, t0=0, dt=0.01, sim_end_time=10, sx0=0, sy0=0, sz0=0):
    global omega0
    global n_hat

    # set initial conditions 
    omega0 = omega0_param # initial angular velocity (rad/s)
    n_hat = n_hat_param # initial angular velocity direction

    v0_vec = np.array([vx0, vy0, vz0], dtype=float) # vector of initial velocities (m/s)
    p0_vec = np.array([sx0, sy0, sz0], dtype=float) # vector of initial positions (m)

    # approximate velocity of object
    vel_approx = RungeKutta4(dvdt, t0, dt, v0_vec)
    vel_approx.sim(sim_end_time)
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
    pos_approx.sim(sim_end_time)
    sx_list, sy_list, sz_list = map(list, zip(*pos_approx.a_list))

    # ensure simulations end when the object hits the ground (assumed to be at y=0)
    new_sy_list = []
    for i in sy_list:
        if i < 0:
            break
        new_sy_list.append(i)
    sy_list = new_sy_list
    vel_approx_tlist, pos_approx_tlist = vel_approx.t_list.copy()[:len(sy_list)], pos_approx.t_list.copy()[:len(sy_list)]
    sx_list, sz_list, vx_list, vy_list, vz_list = sx_list[:len(sy_list)], sz_list[:len(sy_list)], vx_list[:len(sy_list)], vy_list[:len(sy_list)], vz_list[:len(sy_list)]
    
    return vel_approx_tlist, vx_list, vy_list, vz_list, pos_approx_tlist, sx_list, sy_list, sz_list

# plot the outputs of the simulation
def plot_solutions(vel_approx_tlist, vx_list, vy_list, vz_list, pos_approx_tlist, sx_list, sy_list, sz_list):
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


# example usage
vel_approx_tlist, vx_list, vy_list, vz_list, pos_approx_tlist, sx_list, sy_list, sz_list = solve(8, 12, 3)
plot_solutions(vel_approx_tlist, vx_list, vy_list, vz_list, pos_approx_tlist, sx_list, sy_list, sz_list)
