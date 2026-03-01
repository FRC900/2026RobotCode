import numpy as np
import ProjectilePath as pp
import FuelClearance as fc
import torch
import time

runtime_counter = 0 
runtime_samples = 0
samples = 1000
def generate_sample(fuel):
    global runtime_counter
    global runtime_samples
    global samples

    vx_robot = np.random.uniform(-7, 7)
    vy_robot = np.random.uniform(-1, 1)
    vz_robot = np.random.uniform(-7, 7)

    xt = np.random.uniform(0, 10)
    yt = np.random.uniform(0, 5)
    zt = np.random.uniform(0, 10)

    fuel_solver = pp.ProjectileSolver(fuel, xt, yt, zt, 10, 10, 10, 10, vx_frame=vx_robot, vy_frame=vy_robot, vz_frame=vz_robot, fix_speed=True, fix_omega=True, lm_iters=5, sim_end_time=5, dt=0.01, clearance_func=fc.hub_clearance)

    fuel_solver.levenberg_marquardt()

    theta = fuel_solver.theta
    phi = fuel_solver.phi

    vel_approx_tlist, vx_list, vy_list, vz_list, speedf, pos_approx_tlist, sx_list, sy_list, sz_list, omegaf = fuel.trajectory(fuel_solver.vx+vx_robot, fuel_solver.vy+vy_robot, fuel_solver.vz+vz_robot, fuel_solver.omega, dt=0.01, stop_on_y=yt)
    if abs(sx_list[-1]-xt) < 0.15 and abs(sy_list[-1]-yt) < 0.15 and abs(sz_list[-1]-zt) < 0.15:
        return np.array([vx_robot, vy_robot, vz_robot, xt, yt, zt]), np.array([theta, phi])
    

X = []
Y = []
fuel = pp.Projectile(0.0762, 0.226796)
print("Calculating runtime...")
for i in range(samples):
    start = time.perf_counter()
    try:
        x, y = generate_sample(fuel)
        X.append(x)
        Y.append(y)
    except TypeError:
        pass
    end = time.perf_counter()
    runtime_counter += (end-start)
    runtime_samples += 1

    if runtime_samples == 10:
        print(f"ETA: {runtime_counter * (samples-i)/runtime_samples}")
        runtime_samples = 0 
        runtime_counter = 0

X_ARR = np.array(X)
Y_ARR = np.array(Y)

X_TENSOR = torch.tensor(X_ARR, dtype=torch.float32)
Y_TENSOR = torch.tensor(Y_ARR, dtype=torch.float32)
torch.save({
    "X": X_TENSOR,
    "Y": Y_TENSOR
}, "angle_dataset.pt")
