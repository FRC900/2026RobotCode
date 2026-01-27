import FuelPathRK4 as fp
import matplotlib.pyplot as plt

v_list = list(range(40))

x_list = []
for i in range(40):
    vel_approx_tlist, vx_list, vy_list, vz_list, pos_approx_tlist, sx_list, sy_list, sz_list = fp.solve(float(i), 10, 0, omega0_param=(12*3.14))
    x_displacment = sx_list[-1]
    x_list.append(x_displacment)

y_list = []
for i in range(40):
    vel_approx_tlist, vx_list, vy_list, vz_list, pos_approx_tlist, sx_list, sy_list, sz_list = fp.solve(0, float(i), 0, omega0_param=(12*3.14))
    y_displacment = sy_list[-1]
    y_list.append(y_displacment)

z_list = []
for i in range(40):
    vel_approx_tlist, vx_list, vy_list, vz_list, pos_approx_tlist, sx_list, sy_list, sz_list = fp.solve(0, 10, float(i), omega0_param=(12*3.14))
    z_displacment = sz_list[-1]
    z_list.append(z_displacment)


plt.scatter(v_list, x_list)
plt.xlabel("Initial X Velocity (m/s)")
plt.ylabel("Final X Displacement (m)")
plt.title("Final X Displacement vs Initial X Velocity")
plt.grid(True)
plt.show()


plt.scatter(v_list, y_list)
plt.xlabel("Initial Y Velocity (m/s)")
plt.ylabel("Final Y Displacement (m)")
plt.title("Final Y Displacement vs Initial Y Velocity")
plt.grid(True)
plt.show()


plt.scatter(v_list, z_list)
plt.xlabel("Initial Z Velocity (m/s)")
plt.ylabel("Final Z Displacement (m)")
plt.title("Final Z Displacement vs Initial Z Velocity")
plt.grid(True)
plt.show()
