import FuelPathRK4 as fp
import matplotlib.pyplot as plt

'''
(target x,y,z), coarse Cartesian scan (vx,vy,vz,omega) [high spin, low spin, high arc, low arc, high speed, low speed, etc.], 
keep near-hits, 
Levenberg–Marquardt refinement, 
deduplicate, 
return set of solutions, 
check each set of solutions using rk4 and choose fastest shot to y=the hub, 
use optimal solution as input to next problem (choose path requiring least to no turret adjsutment [does_current_path_work function])
make an nn to speed this up lol?
'''


