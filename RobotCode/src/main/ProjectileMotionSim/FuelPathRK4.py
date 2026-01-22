# Define Constants 

rho = 0
ball_sarea = 0
ball_mass = 0
g = 0
vx0 = 0
vy0 = 0
vz0 = 0
omega0 = 0
I = 0
n_hat = 1
ball_radius = 0
mu = 0

class FuelSim:
    def __init__(self):
        # t and a are initialized to the differential equation solution's initial conditions
        
        self.t = None
        self.a = None
        self.dt = None # timestep
        self.dadt = None # differential equation
        self.t_list = [self.t] # list of times
        self.a_list = [self.a] # list of rk4-approximations

    def rk4(self):
        k1 = self.dt*self.dadt(self.t, self.a)
        k2 = self.dt*self.dadt(self.t+self.dt/2., self.a+k1/2.)
        k3 = self.dt*self.dadt(self.t+self.dt/2., self.a+k2/2.)
        k4 = self.dt*self.dadt(self.t+self.dt, self.a+k3)
        
        a = a + ((k1+2*k2+2*k3+k4)/6.)
        t = t + self.dt
        return t, a
    
    def sim(self, timelen=False):
        while self.t != timelen:
            self.t, self.a = self.rk4()
            self.t_list.append(self.t)
            self.a_list.append(self.a)
            self.t += self.dt
