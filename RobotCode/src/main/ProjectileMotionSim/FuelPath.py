# example usage of ProjectilePath.py

import ProjectilePath as pp
from numpy import rad2deg
import time 

# define targets and initial condition guesses
xt = 10
yt = 2
zt = 10

# define initial solution guesses
vxi0 = 10
vyi0 = 10
vzi0 = 10
omegai0 = 10

# define fuel object and fuel_solver object
fuel = pp.Projectile(0.0762, 0.226796)
fuel_solver = pp.ProjectileSolver(fuel, xt, yt, zt, vxi0, vyi0, vzi0, omegai0, lm_iters=3, sim_end_time=5, dt=0.01, clearance_func=pp.hub_clearance)

# solve for valid inputs and time the solver
start = time.perf_counter()
fuel_solver.levenberg_marquardt()
end = time.perf_counter()
print("\nLevenberg-Marquardt Time: ", 1000 * (end - start), "ms")

# display results 
vel_approx_tlist, vx_list, vy_list, vz_list, speedf, pos_approx_tlist, sx_list, sy_list, sz_list, omegaf = fuel.trajectory(fuel_solver.vx, fuel_solver.vy, fuel_solver.vz, fuel_solver.omega, dt=0.001, stop_on_y=yt) # smaller dt=more refined approximation
print("\nInitial X-Velocity:", fuel_solver.vx, "m/s", "\nInitial Y-Velocity:", fuel_solver.vy, "m/s", "\nInitial Z-Velocity:", fuel_solver.vz, "m/s", "\nInitial Spin (+, backspin | -, topspin):", fuel_solver.omega, "rad/s", "\nTheta:", rad2deg(fuel_solver.theta), "deg", "\nPhi:", rad2deg(fuel_solver.phi), "deg")
print("\nFinal X-Position:", sx_list[-1], "m", "\nFinal Y-Position:", sy_list[-1], "m", "\nFinal Z-Position:", sz_list[-1], "m", "\n")
fuel.plot_solutions(vel_approx_tlist, vx_list, vy_list, vz_list, pos_approx_tlist, sx_list, sy_list, sz_list)
