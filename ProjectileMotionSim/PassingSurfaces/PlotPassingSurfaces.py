import numpy as np
import matplotlib.pyplot as plt

# load data
data = np.load("ProjectileMotionSim/PassingSurfaces/clean_theta_phi_surface.npz")
r_vals = data['r_vals']
vf_vals = data['vf_vals']
vl_vals = data['vl_vals']
theta_surface = data['theta_surface']
phi_surface = data['phi_surface']

# forward (radial) velocity, distance to target --> theta
theta_slice = theta_surface[:, 0, :] 
R, VL = np.meshgrid(r_vals, vl_vals, indexing='ij')

fig1 = plt.figure(figsize=(10,6))
ax1 = fig1.add_subplot(111, projection='3d')
surf1 = ax1.plot_surface(R, VL, theta_slice, cmap='viridis', edgecolor='k')
ax1.set_xlabel('Distance to Hub r (m)')
ax1.set_ylabel('Lateral Robot Velocity vl (m/s)')
ax1.set_zlabel('Azimuthal Angle θ (rad)')
ax1.set_title('Azimuthal Angle (Theta) Surface vs Distance and\nLateral Velocity (launch speed = 22.5 m/s)')
fig1.colorbar(surf1, ax=ax1, shrink=0.5, aspect=10)
fig1.savefig("ProjectileMotionSim/PassingSurfaces/theta_surface.png", dpi=300, bbox_inches='tight') # save plot 1

# lateral (tangential) velocity, distance to target --> phi
phi_slice = phi_surface[:, :, 0]
R, VF = np.meshgrid(r_vals, vf_vals, indexing='ij')

fig2 = plt.figure(figsize=(10,6))
ax2 = fig2.add_subplot(111, projection='3d')
surf2 = ax2.plot_surface(R, VF, phi_slice, cmap='plasma', edgecolor='k')
ax2.set_xlabel('Distance to Hub r (m)')
ax2.set_ylabel('Forward Robot Velocity vf (m/s)')
ax2.set_zlabel('Launch Angle φ (rad)')
ax2.set_title('Launch Angle (Phi) Surface vs Distance and\nForward Velocity (launch speed = 22.5 m/s)')
fig2.colorbar(surf2, ax=ax2, shrink=0.5, aspect=10)
fig2.savefig("ProjectileMotionSim/PassingSurfaces/phi_surface.png", dpi=300, bbox_inches='tight') # save plot 2

plt.show()
