import torch
import torch.nn as nn
import torch.optim as optim

class ShooterNet(nn.Module):
    def __init__(self):
        super().__init__()
        self.net = nn.Sequential(
            nn.Linear(6, 128),
            nn.ReLU(),
            nn.Linear(128, 128),
            nn.ReLU(),
            nn.Linear(128, 2)
        )

    def forward(self, x):
        return self.net(x)

model = ShooterNet()

optimizer = optim.Adam(model.parameters(), lr=1e-3)
loss_fn = nn.MSELoss()

checkpoint = torch.load("angle_dataset.pt")

X = checkpoint["X"]
Y = checkpoint["Y"]

for epoch in range(200):
    optimizer.zero_grad()
    preds = model(X)
    loss = loss_fn(preds, Y)
    loss.backward()
    optimizer.step()

    if epoch % 20 == 0:
        print("Epoch:", epoch, "Loss:", loss.item())

torch.save(model.state_dict(), "shooter_model.pt")
model.eval()
scripted_model = torch.jit.script(model)
scripted_model.save("shooter_scripted.pt")
