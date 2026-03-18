'''
Uses the simulated surfaces to generate a polynomial
to fit them. Drops outliers and NaN values. 
'''

import numpy as np
from sklearn.preprocessing import PolynomialFeatures
from sklearn.linear_model import LinearRegression
from sklearn.pipeline import Pipeline
from sklearn.metrics import r2_score
import pickle
import json
import os

def fit_surfaces(npz_path, name, poly_degree=4):
    data = np.load(npz_path)
    r_vals, vf_vals, vl_vals = data['r_vals'], data['vf_vals'], data['vl_vals']
    theta, phi = data['theta_surface'], data['phi_surface']

    # dropping NaNs
    R, VF, VL = np.meshgrid(r_vals, vf_vals, vl_vals, indexing='ij')
    valid = np.isfinite(theta) & np.isfinite(phi)
    r, vf, vl = R[valid], VF[valid], VL[valid]
    theta_all, phi_all = theta[valid], phi[valid]

    # Outlier removal: per-r-slice Tukey IQR fence on phi
    # (theta has no outliers - they only appeared in phi)
    TUKEY_CONSTANT = 1.5
    keep = np.ones(len(r), dtype=bool)
    for r_val in r_vals:
        idx = np.where(r == r_val)[0]
        if len(idx) < 5:
            continue
        p = phi_all[idx]
        q1, q3 = np.percentile(p, [25, 75])
        iqr = q3 - q1
        keep[idx] = (p >= q1 - TUKEY_CONSTANT * iqr) & (p <= q3 + TUKEY_CONSTANT * iqr)
    print(f"{name} -- Outliers removed: {len(r) - keep.sum()} / {len(r)}  ->  {keep.sum()} clean points")

    r, vf, vl = r[keep], vf[keep], vl[keep]
    theta_c, phi_c = theta_all[keep], phi_all[keep]

    def poly_fit(X, y):
        model = Pipeline([('poly', PolynomialFeatures(poly_degree)), ('reg', LinearRegression())])
        model.fit(X, y)
        return model, r2_score(y, model.predict(X))

    # phi(r, vf) --> phi 
    phi_model, r2_phi   = poly_fit(np.column_stack([r, vf]), phi_c)
    # theta(vf, vl) --> theta 
    theta_model, r2_theta = poly_fit(np.column_stack([r, vl]), theta_c)

    print(f"{name} -- phi R2 = {r2_phi:.6f} (inputs: r, vf)")
    print(f"{name} -- theta R2 = {r2_theta:.6f} (inputs: vf, vl)")
    return phi_model, theta_model

def export_model(model, path):
    poly = model.named_steps['poly']
    reg  = model.named_steps['reg']
    json.dump({
        'intercept': reg.intercept_,
        'coefs': reg.coef_.tolist(),
        'powers': poly.powers_.tolist()
    }, open(path, 'w'))


base = 'ProjectileMotionSim/TrajectorySurfaces'
dir_list = ["20_RPS", "30_RPS", "40_RPS", "50_RPS", "60_RPS", "70_RPS", "80_RPS", "90_RPS"]

for dir in dir_list:
    phi_model, theta_model = fit_surfaces(f'{base}/{dir}/clean_theta_phi_surface.npz', dir)

    with open(f'{base}/{dir}/models/phi_model.pkl', 'wb') as f:
        pickle.dump(phi_model, f)

    with open(f'{base}/{dir}/models/theta_model.pkl', 'wb') as f:
        pickle.dump(theta_model, f)

    export_model(phi_model, f'{base}/{dir}/models/phi_model.json')
    export_model(theta_model, f'{base}/{dir}/models/theta_model.json')
