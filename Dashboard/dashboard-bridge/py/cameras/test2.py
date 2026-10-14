from ultralytics import YOLO

model = YOLO(r"C:\Users\Usuario\Cerberus\Dashboard\dashboard-bridge\py\cameras\LimeProj3.pt")

model.predict(source=1, conf=0.4, show=True)