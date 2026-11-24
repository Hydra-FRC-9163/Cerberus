from ultralytics import YOLO

model = YOLO(r"C:\Users\Usuario\Cerberus\Dashboard\dashboard-bridge\py\cameras\pegaRoboBest.pt")

model.predict(source=0, conf=0.4, show=True)