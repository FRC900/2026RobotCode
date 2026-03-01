import torch
import time 
import ProjectilePath as pp
import FuelClearance as fc
import numpy as np

fuel = pp.Projectile(0.0762, 0.226796)

model = torch.jit.load("shooter_scripted.pt")
model.eval()

def predict_angles(vx_robot, vy_robot, vz_robot, xt, yt, zt):
    inp = torch.tensor(
        [[vx_robot, vy_robot, vz_robot, xt, yt, zt]],
        dtype=torch.float32
    )

    with torch.no_grad():
        start = time.perf_counter()
        output = model(inp)[0]
        end = time.perf_counter()
        theta = output[0].item()
        phi = output[1].item()

    return theta, phi, (end - start)

if __name__ == "__main__":
    unaccurate = 0
    accurate = 0
    for i in range(0, 10):
        for j in range(0, 10):
            for k in range(-7, 7):
                for g in range(-7, 7):
                    theta, phi, t = predict_angles(k, 0.0, g, i, 2.0, j)
                    fuel_solver = pp.ProjectileSolver(fuel, i, 2, j, 10, 10, 10, 10, vx_frame=k, vy_frame=0, vz_frame=g, fix_speed=True, fix_omega=True, lm_iters=5, sim_end_time=5, dt=0.01, clearance_func=fc.hub_clearance)
                    fuel_solver.levenberg_marquardt()
                    if np.abs(fuel_solver.theta - theta) < 0.18 and np.abs(fuel_solver.phi - phi) < 0.18:
                        accurate += 1
                    else:
                        unaccurate += 1

    print("Accurate simulations:", accurate, "\n", "Inaccurate simulations:", unaccurate, "\n") 
