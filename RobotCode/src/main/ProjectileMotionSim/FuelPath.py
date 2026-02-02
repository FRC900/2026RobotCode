import ProjectilePath as pp
import time 

fuel = pp.Projectile(0.0762, 0.226796)
fuel_solver = pp.ProjectileSolver(fuel, 10, 0, 9, 5, 2, 4, 20, lm_iters=3, stop_on_y_zero=True)

start = time.time()
fuel_solver.levenberg_marquardt()
end = time.time()
print(end - start)

vel_approx_tlist, vx_list, vy_list, vz_list, pos_approx_tlist, sx_list, sy_list, sz_list = fuel.trajectory(fuel_solver.vx, fuel_solver.vy, fuel_solver.vz, fuel_solver.omega, stop_on_y_zero=True)
fuel.plot_solutions(vel_approx_tlist, vx_list, vy_list, vz_list, pos_approx_tlist, sx_list, sy_list, sz_list)
print(sx_list[-1], sz_list[-1])

# cache
# remove omega0 and nhat from projectile
# constrain vx vy vz angles clearance
