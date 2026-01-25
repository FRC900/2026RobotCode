import numpy as np 

# Define Constants 
rho = 0
ball_radius = 0
ball_sarea = 0
ball_mass = 0
g = 0
t0 = 0
vx0 = 0
vy0 = 0
vz0 = 0
v0_vec = np.array([vx0, vy0, vz0])
omega0 = 0
I = 0
n_hat = 1
mu = 0
dt = 0.01


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

def spin_coeff(Re):
    return Re

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

def drag_coeff(Re):
    return Re

def lift_coeff(S):
    return S
    
def dvxdt(t, speed, vx, vy):
    coeff = (-1*rho*ball_sarea*speed) / (2*ball_mass)
    return coeff * ((drag_coeff(reynolds(speed))*vx) + (lift_coeff(shear(t, speed))*vy))

def dvydt(t, speed, vx, vy):
    coeff = (rho*ball_sarea*speed) / (2*ball_mass)
    return coeff * ((-1*drag_coeff(reynolds(speed))*vy) + (lift_coeff(shear(t, speed))*vx)) - g 

def dvzdt(speed, vz):
    coeff = (-1*rho) / (2*ball_mass)
    return coeff * drag_coeff(reynolds(speed)) * ball_sarea * speed * vz

def dvdt(t, a):
    vx, vy, vz = a
    speed = np.linalg.norm(a)

    dvx = dvxdt(t, speed, vx, vy)
    dvy = dvydt(t, speed, vx, vy)
    dvz = dvzdt(speed, vz)

    return np.array([dvx, dvy, dvz])


vel_approx = RungeKutta4(dvdt, t0, dt, v0_vec)
