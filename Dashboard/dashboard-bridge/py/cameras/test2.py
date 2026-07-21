from ultralytics import YOLO

model = YOLO(r"C:\Users\Usuario\Cerberus\Dashboard\dashboard-bridge\py\cameras\ChargedUp.pt")

model.predict(source=0, conf=0.40, show=True)