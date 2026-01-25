import numpy as np 
import matplotlib.pyplot as plt

# Define Constants 
ball_radius = 0.150114
ball_farea = np.pi * (ball_radius**2)
ball_mass = 0.21500278
g = 9.81
t0 = 0
vx0 = 8
vy0 = 5
vz0 = 2
v0_vec = np.array([vx0, vy0, vz0])
omega0 = 12 * np.pi
I = 0.4 * ball_mass * (ball_radius**2)
n_hat = 1
rho = 1.195
mu = 1.835e-5
dt = 0.01
vel_sim_end_time = 10
pos_sim_end_time = 10


class RungeKutta4:
    def __init__(self, dadt, t0, dt, a0):
        # t and a are initialized to the differential equation solution's initial conditions
        
        self.t = t0 # time
        self.a = a0 # rk4 approximation
        self.dt = dt # timestep
        self.dadt = dadt # differential equation

        self.t_list = [self.t] # list of times
        self.a_list = [self.a] # list of RK4-approximations

    def rk4(self):
        k1 = self.dt*self.dadt(self.t, self.a)
        k2 = self.dt*self.dadt(self.t+self.dt/2., self.a+k1/2.)
        k3 = self.dt*self.dadt(self.t+self.dt/2., self.a+k2/2.)
        k4 = self.dt*self.dadt(self.t+self.dt, self.a+k3)
        
        self.a = self.a + ((k1+2*k2+2*k3+k4)/6.)
        self.t += self.dt

    def sim(self, timelen):
        while self.t <= timelen:
            self.rk4()
            self.t_list.append(self.t)
            self.a_list.append(self.a.copy())



def reynolds(speed):
    return (rho*speed*2*ball_radius) / mu

def spin_coeff(Re): # complete
    return 500

def omega(t, speed):
    if omega0 < 1e-8:
        return 0.0
    denom1 = 1 / omega0
    denom2 = (t*spin_coeff(reynolds(speed))*rho*(ball_radius**5)) / (2*I) 
    return n_hat / (denom1+denom2)

def shear(t, speed):
    if speed < 1e-8:
        return 0.0
    return (np.abs(omega(t, speed))*ball_radius) / speed

def drag_coeff(Re): # complete
    return 0.47

def lift_coeff(S): # complete
    return 1.5 * S
    
def dvxdt(t, speed, vx, vy):
    coeff = (-1*rho*ball_farea*speed) / (2*ball_mass)
    return coeff * ((drag_coeff(reynolds(speed))*vx) + (lift_coeff(shear(t, speed))*vy))

def dvydt(t, speed, vx, vy):
    coeff = (rho*ball_farea*speed) / (2*ball_mass)
    return coeff * ((-1*drag_coeff(reynolds(speed))*vy) + (lift_coeff(shear(t, speed))*vx)) - g 

def dvzdt(speed, vz):
    coeff = (-1*rho) / (2*ball_mass)
    return coeff * drag_coeff(reynolds(speed)) * ball_farea * speed * vz

def dvdt(t, a):
    vx, vy, vz = a
    speed = np.linalg.norm(a)

    dvx = dvxdt(t, speed, vx, vy)
    dvy = dvydt(t, speed, vx, vy)
    dvz = dvzdt(speed, vz)

    return np.array([dvx, dvy, dvz])


vel_approx = RungeKutta4(dvdt, t0, dt, v0_vec)
vel_approx.sim(vel_sim_end_time)
vx_list, vy_list, vz_list = map(list, zip(*vel_approx.a_list))

plt.scatter(vel_approx.t_list, vx_list)
plt.xlabel("Time (s)")
plt.ylabel("X-Velocity (m/s)")
plt.show()

plt.scatter(vel_approx.t_list, vy_list)
plt.xlabel("Time (s)")
plt.ylabel("Y-Velocity (m/s)")
plt.show()

plt.scatter(vel_approx.t_list, vz_list)
plt.xlabel("Time (s)")
plt.ylabel("Z-Velocity (m/s)")
plt.show()
