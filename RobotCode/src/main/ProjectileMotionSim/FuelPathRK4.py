import numpy as np 
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D

# Define Constants 
ball_radius = 0.150114
ball_farea = np.pi * (ball_radius**2)
ball_mass = 0.21500278
g = 9.81
t0 = 0
vx0 = 8
vy0 = 5
vz0 = 2
sx0 = 0
sy0 = 0
sz0 = 0
v0_vec = np.array([vx0, vy0, vz0], dtype=float)
p0_vec = np.array([sx0, sy0, sz0], dtype=float)
omega0 = 12 * np.pi
I = 0.4 * ball_mass * (ball_radius**2)
n_hat = 1
rho = 1.195
mu = 1.835e-5
dt = 0.01
sim_end_time = 10


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

    return np.array([dvx, dvy, dvz], dtype=float)

vel_approx = RungeKutta4(dvdt, t0, dt, v0_vec)
vel_approx.sim(sim_end_time)
v_list = vel_approx.a_list
vx_list, vy_list, vz_list = map(list, zip(*vel_approx.a_list))


def dsdt(t, _):
    idx = int(t / dt)

    dsx = v_list[idx][0]
    dsy = v_list[idx][1]
    dsz = v_list[idx][2]

    return np.array([dsx, dsy, dsz], dtype=float)

pos_approx = RungeKutta4(dsdt, t0, dt, p0_vec)
pos_approx.sim(sim_end_time)
sx_list, sy_list, sz_list = map(list, zip(*pos_approx.a_list))

sy_list = [i for i in sy_list if i >= 0]
vel_approx_tlist, pos_approx_tlist = vel_approx.t_list.copy()[:len(sy_list)], pos_approx.t_list.copy()[:len(sy_list)]
sx_list, sz_list, vx_list, vy_list, vz_list = sx_list[:len(sy_list)], sz_list[:len(sy_list)], vx_list[:len(sy_list)], vy_list[:len(sy_list)], vz_list[:len(sy_list)]


gy_list = [(vy0*i*dt-(0.5*g*((i*dt)**2))) for i in range(int(2*vy0/(g*dt))+1)]
gx_list = [vx0*i*dt for i in range(len(gy_list))]
gz_list = [vz0*i*dt for i in range(len(gy_list))]


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

ax.scatter(sx_list, sy_list, sz_list, label='Full Trajectory', color='magenta', s=1)
ax.scatter(gx_list, gy_list, gz_list, label='Gravity Only (DVAT) Trajectory', color='orange', s=1)

ax.set_xlabel('X Position (m)')
ax.set_ylabel('Y Position (m)')
ax.set_zlabel('Z Position (m)')
ax.set_title('3D Trajectory')
ax.legend()

plt.show()
