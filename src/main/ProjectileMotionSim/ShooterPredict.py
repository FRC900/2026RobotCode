import torch
import time 

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
    theta, phi, t = predict_angles(0.0, 0.0, 0.0, 10.0, 2.0, 10.0)
    print("Theta:", theta)
    print("Phi:", phi)
    print("Predict Speed (ms):", t*1000)
