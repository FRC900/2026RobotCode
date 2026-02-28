# example usage of ProjectilePath.py for FRC 2026 Fuel

import ProjectilePath as pp
import FuelClearance as fc
from numpy import array, rad2deg, linalg, pi
from tabulate import tabulate
import time 

# define targets and initial condition guesses
xt = 12
yt = 2
zt = 0

# define robot's initial velocities and Fuel initial spin
vx_robot = -10
vy_robot = 0
vz_robot = -10

v_mag_robot = linalg.norm(array([vx_robot, vy_robot, vz_robot]))

# define initial solution guesses
vxi0 = 18
vyi0 = 18
vzi0 = 18
omegai0 = 3 * 2*pi

# define fuel object and fuel_solver object
fuel = pp.Projectile(0.0762, 0.226796)
fuel_solver = pp.ProjectileSolver(fuel, xt, yt, zt, vxi0, vyi0, vzi0, omegai0, vx_frame=vx_robot, vy_frame=vy_robot, vz_frame=vz_robot, fix_speed=True, fix_omega=True, lm_iters=5, sim_end_time=5, dt=0.01, clearance_func=fc.hub_clearance)

# solve for valid inputs and time the solver
start = time.perf_counter()
fuel_solver.levenberg_marquardt()
end = time.perf_counter()

# display results 
vel_approx_tlist, vx_list, vy_list, vz_list, speedf, pos_approx_tlist, sx_list, sy_list, sz_list, omegaf = fuel.trajectory(fuel_solver.vx+vx_robot, fuel_solver.vy+vy_robot, fuel_solver.vz+vz_robot, fuel_solver.omega, dt=0.01, stop_on_y=yt) # smaller dt=more refined approximation

table = [
    ["LM Time (ms)", 1000 * (end - start)],
    ["Shot Speed (m/s)", fuel_solver.v_mag],
    ["Shot vx (m/s)", fuel_solver.vx],
    ["Shot vy (m/s)", fuel_solver.vy],
    ["Shot vz (m/s)", fuel_solver.vz],
    ["Shot Spin (rad/s)", fuel_solver.omega],
    ["Total Speed (m/s)", fuel_solver.v_mag + v_mag_robot],
    ["Total vx (m/s)", fuel_solver.vx + vx_robot],
    ["Total vy (m/s)", fuel_solver.vy + vy_robot],
    ["Total vz (m/s)", fuel_solver.vz + vz_robot],
    ["Theta (deg)", rad2deg(fuel_solver.theta)],
    ["Phi (deg)", rad2deg(fuel_solver.phi)],
    ["Final X (m)", sx_list[-1]],
    ["Final Y (m)", sy_list[-1]],
    ["Final Z (m)", sz_list[-1]],
]

print(tabulate(table, headers=["Parameter", "Value"], tablefmt="rounded_grid"))
fuel.plot_solutions(vel_approx_tlist, vx_list, vy_list, vz_list, pos_approx_tlist, sx_list, sy_list, sz_list)
