import numpy as np
import matplotlib.pyplot as plt
import pickle

# load data
data = np.load("ProjectileMotionSim/ShooterSurfaces/clean_theta_phi_surface.npz")
r_vals = data['r_vals']
vf_vals = data['vf_vals']
vl_vals = data['vl_vals']
theta_surface = data['theta_surface']
phi_surface = data['phi_surface']

# load models
with open('ProjectileMotionSim/ShooterSurfaces/models/phi_model.pkl', 'rb') as f:
    phi_model = pickle.load(f)
with open('ProjectileMotionSim/ShooterSurfaces/models/theta_model.pkl', 'rb') as f:
    theta_model = pickle.load(f)

# forward (radial) velocity, distance to target --> theta
theta_slice = theta_surface[:, 0, :] 
R, VL = np.meshgrid(r_vals, vl_vals, indexing='ij')

fig1 = plt.figure(figsize=(10,6))
ax1 = fig1.add_subplot(111, projection='3d')
surf1 = ax1.plot_surface(R, VL, theta_slice, cmap='viridis', edgecolor='k')
ax1.set_xlabel('Distance to Hub r (m)')
ax1.set_ylabel('Lateral Robot Velocity vl (m/s)')
ax1.set_zlabel('Azimuthal Angle θ (rad)')
ax1.set_title('Azimuthal Angle (Theta) Surface vs Distance and\nLateral Velocity (launch speed = 14.7 m/s)')
fig1.colorbar(surf1, ax=ax1, shrink=0.5, aspect=10)
fig1.savefig("ProjectileMotionSim/ShooterSurfaces/plots/theta_surface.png", dpi=300, bbox_inches='tight') # save plot 1

# lateral (tangential) velocity, distance to target --> phi
phi_slice = phi_surface[:, :, 0]
R, VF = np.meshgrid(r_vals, vf_vals, indexing='ij')

fig2 = plt.figure(figsize=(10,6))
ax2 = fig2.add_subplot(111, projection='3d')
surf2 = ax2.plot_surface(R, VF, phi_slice, cmap='plasma', edgecolor='k')
ax2.set_xlabel('Distance to Hub r (m)')
ax2.set_ylabel('Forward Robot Velocity vf (m/s)')
ax2.set_zlabel('Launch Angle φ (rad)')
ax2.set_title('Launch Angle (Phi) Surface vs Distance and\nForward Velocity (launch speed = 14.7 m/s)')
fig2.colorbar(surf2, ax=ax2, shrink=0.5, aspect=10)
fig2.savefig("ProjectileMotionSim/ShooterSurfaces/plots/phi_surface.png", dpi=300, bbox_inches='tight') # save plot 2

# theta(r, vl) --> theta
R_t, VL_t = np.meshgrid(r_vals, vl_vals, indexing='ij')
X_theta = np.column_stack([R_t.ravel(), VL_t.ravel()])
theta_fit = theta_model.predict(X_theta).reshape(R_t.shape)

# phi(r, vf) --> phi
R_p, VF_p = np.meshgrid(r_vals, vf_vals, indexing='ij')
X_phi = np.column_stack([R_p.ravel(), VF_p.ravel()])
phi_fit = phi_model.predict(X_phi).reshape(R_p.shape)

# plot 3: theta data + fit overlay
fig3 = plt.figure(figsize=(10,6))
ax3 = fig3.add_subplot(111, projection='3d')
ax3.plot_surface(R_t, VL_t, theta_slice, cmap='viridis', alpha=0.6, edgecolor='none')
ax3.plot_surface(R_t, VL_t, theta_fit,   cmap='cool',    alpha=0.6, edgecolor='none')
ax3.set_xlabel('Distance to Hub r (m)')
ax3.set_ylabel('Lateral Robot Velocity vl (m/s)')
ax3.set_zlabel('Azimuthal Angle θ (rad)')
ax3.set_title('Theta: Data (viridis) vs Polynomial Fit (cool) (launch speed = 14.7 m/s)')
fig3.savefig("ProjectileMotionSim/ShooterSurfaces/plots/theta_overlay.png", dpi=300, bbox_inches='tight')

# plot 4: theta fit only
fig4 = plt.figure(figsize=(10,6))
ax4 = fig4.add_subplot(111, projection='3d')
surf4 = ax4.plot_surface(R_t, VL_t, theta_fit, cmap='viridis', edgecolor='k')
ax4.set_xlabel('Distance to Hub r (m)')
ax4.set_ylabel('Lateral Robot Velocity vl (m/s)')
ax4.set_zlabel('Azimuthal Angle θ (rad)')
ax4.set_title('Theta Polynomial Fit (launch speed = 14.7 m/s)')
fig4.colorbar(surf4, ax=ax4, shrink=0.5, aspect=10)
fig4.savefig("ProjectileMotionSim/ShooterSurfaces/plots/theta_fit.png", dpi=300, bbox_inches='tight')

# plot 5: phi data + fit overlay
fig5 = plt.figure(figsize=(10,6))
ax5 = fig5.add_subplot(111, projection='3d')
ax5.plot_surface(R_p, VF_p, phi_slice, cmap='plasma', alpha=0.6, edgecolor='none')
ax5.plot_surface(R_p, VF_p, phi_fit,   cmap='cool',   alpha=0.6, edgecolor='none')
ax5.set_xlabel('Distance to Hub r (m)')
ax5.set_ylabel('Forward Robot Velocity vf (m/s)')
ax5.set_zlabel('Launch Angle φ (rad)')
ax5.set_title('Phi: Data (plasma) vs Polynomial Fit (cool) (launch speed = 14.7 m/s)')
fig5.savefig("ProjectileMotionSim/ShooterSurfaces/plots/phi_overlay.png", dpi=300, bbox_inches='tight')

# plot 6: phi fit only
fig6 = plt.figure(figsize=(10,6))
ax6 = fig6.add_subplot(111, projection='3d')
surf6 = ax6.plot_surface(R_p, VF_p, phi_fit, cmap='plasma', edgecolor='k')
ax6.set_xlabel('Distance to Hub r (m)')
ax6.set_ylabel('Forward Robot Velocity vf (m/s)')
ax6.set_zlabel('Launch Angle φ (rad)')
ax6.set_title('Phi Polynomial Fit (launch speed = 14.7 m/s)')
fig6.colorbar(surf6, ax=ax6, shrink=0.5, aspect=10)
fig6.savefig("ProjectileMotionSim/ShooterSurfaces/plots/phi_fit.png", dpi=300, bbox_inches='tight')

plt.show()
