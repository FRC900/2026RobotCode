"""
Creates 3D surfaces relating: 
- forward (radial) frame velocity
- lateral (tangential) velocity
- radial distance to target
to
- theta
- phi

This is essentially a surrogate model for the 
main solver. It's like a lookup table to make 
trajectory profiling easier. 
"""

import sys
import os

# allows imports of files from parent directory 
sys.path.append(os.path.dirname(os.path.dirname(__file__)))

import numpy as np
import ProjectilePath as pp
import FuelClearance as fc
import time

hub_height = 1.8288 # m
shot_speed = 14.7 # m/s
shot_spin = 17 * 2*np.pi # rad/s
y0 = 0.4464568922 # m, shooter height 

fuel = pp.Projectile(0.0762, 0.226796) # FRC 2026 Fuel object

r_vals  = np.linspace(0, 6.5, 26) # radial distance to target
vf_vals = np.linspace(-5.5, 5.5, 22) # forward (radial) velocity
vl_vals = np.linspace(-5.5, 5.5, 22) # lateral (tangential) velocity

theta_surface = np.zeros((len(r_vals),
                          len(vf_vals),
                          len(vl_vals)))

phi_surface = np.zeros_like(theta_surface)

# iterate over all distances and speeds to solve for theta and phi
runtime_counter = 0 
runtime_samples = 0
samples = r_vals.shape[0] * vf_vals.shape[0] * vl_vals.shape[0]
bad_line_count = 0
print("Calculating Runtime...")
for i, r in enumerate(r_vals):
    for j, vf in enumerate(vf_vals):
        for k, vl in enumerate(vl_vals):
            start = time.perf_counter()

            # target in rotated frame fixed on the target
            xt = r
            yt = hub_height
            zt = 0

            # initial velocity guesses
            direction = np.array([xt, yt, zt])
            direction /= np.linalg.norm(direction)

            vxi0 = shot_speed * direction[0]
            vyi0 = shot_speed * direction[1]
            vzi0 = shot_speed * direction[2]

            solver = pp.ProjectileSolver(
                fuel,
                xt, yt, zt,
                vxi0, vyi0, vzi0,
                shot_spin,
                sy0=y0,
                vx_frame=vf,
                vy_frame=0,
                vz_frame=vl,
                fix_speed=True,
                fix_omega=True,
                phi_bounds=(0.785398, 1.309),
                theta_bounds=(0, 5.93411946),
                lm_iters=5,
                sim_end_time=5,
                dt=0.01,
                clearance_func=fc.hub_clearance
            )

            # solve for theta and phi
            solver.levenberg_marquardt()

            # check for accuracy
            vel_approx_tlist, vx_list, vy_list, vz_list, speedf, pos_approx_tlist, sx_list, sy_list, sz_list, omegaf = fuel.trajectory(solver.vx+vf, solver.vy, solver.vz+vl, solver.omega, dt=0.01, stop_on_y=yt)
            if abs(sx_list[-1]-xt) < 0.5 and abs(sy_list[-1]-yt) < 0.5 and abs(sz_list[-1]-zt) < 0.5:
                theta_surface[i,j,k] = solver.theta
                phi_surface[i,j,k] = solver.phi
            else:
                theta_surface[i,j,k] = np.nan
                phi_surface[i,j,k] = np.nan
                bad_line_count += 1

            # calculate program ETA 
            end = time.perf_counter()
            runtime_counter += (end-start)
            runtime_samples += 1
            if runtime_samples == 100:
                print(f"ETA (s): {runtime_counter/runtime_samples * (samples - (i*len(vf_vals)*len(vl_vals) + j*len(vl_vals) + k + 1)):.1f}")
                runtime_samples = 0 
                runtime_counter = 0

# save
np.savez("ProjectileMotionSim/ShooterSurfaces/clean_theta_phi_shooter_surface.npz",
         r_vals=r_vals,
         vf_vals=vf_vals,
         vl_vals=vl_vals,
         theta_surface=theta_surface,
         phi_surface=phi_surface)

print(f"Levenberg-Marquardt model failed on {bad_line_count} simulations.")
