from asyncore import read
from genericpath import exists
from tkinter import ttk
from tkinter import *
import json
import os

root = Tk()

style = ttk.Style(root)

root.tk.call("source", "sun-valley.tcl")
root.tk.call("set_theme", "dark")
# Set the theme with the theme_use method
#style.theme_use('sun-valley')

displayHeight = root.winfo_screenheight()
displayWidth = root.winfo_screenwidth()

height = int(displayHeight / 2.8)
width = int(displayWidth / 2.8)

x = int((displayWidth/2) - (width/2))
y = int((displayHeight/2) - (height/2))

root.geometry(f'{width}x{height}+{x}+{y}')
root.resizable(False, False)

canvas = Canvas(root)
canvas.pack()

def round_rectangle(x1, y1, x2, y2, radius=25, **kwargs):
        
    points = [x1+radius, y1,
              x1+radius, y1,
              x2-radius, y1,
              x2-radius, y1,
              x2, y1,
              x2, y1+radius,
              x2, y1+radius,
              x2, y2-radius,
              x2, y2-radius,
              x2, y2,
              x2-radius, y2,
              x2-radius, y2,
              x1+radius, y2,
              x1+radius, y2,
              x1, y2,
              x1, y2-radius,
              x1, y2-radius,
              x1, y1+radius,
              x1, y1+radius,
              x1, y1]

    return canvas.create_polygon(points, **kwargs, smooth=True)

#my_rectangle = round_rectangle(50, 50, 150, 100, radius=20, fill="blue")

root.title("Moon UID")

def proccessInput():
    providedUid = entry.get()

    newUid = {"uid": f"{providedUid}"}

    path = os.path.join(os.getenv("APPDATA"), ".moon")
    file = os.path.join(path, "uid.json")
    if not os.path.exists(path):
        os.makedirs(path)
    
    if not os.path.isfile(file):
        with open(file, 'w') as f:
            jsonString = json.dumps(newUid, indent=2)
            f.write(jsonString)
            f.close()
    else:
        with open(file, 'w') as f:
            jsonString = json.dumps(newUid, indent=2)
            f.write(jsonString)
            f.close()

    

    
    #if requests.get(f'https://auth.drifty.dev/uid/{result}') == '':
        #print(result)

entry = ttk.Entry(root, state="UId", justify='center')
entry.pack()

btnRead = ttk.Button(root, text="Install", command=proccessInput)
btnRead.pack()
btnRead.place(height=20, width=100, x=width/2, y=height/2)


#combo_list = ["Combobox", "Editable item 1", "Editable item 2"]
#combobox = ttk.Combobox(root, values=combo_list)
#combobox.current(0)
#combobox.pack()

root.iconphoto(False, PhotoImage(file = 'key.png'))
root.mainloop()