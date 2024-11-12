import time
import Main_task
import Task1
import Task2
import Task3

time_start = time.time()
for i in range(100):
    Main_task.convert()
time_end = time.time()
print('Main_task: ' + str(time_end - time_start))

time_start = time.time()
for i in range(100):
    Task1.convert()
time_end = time.time()
print('Task1: ' + str(time_end - time_start))

time_start = time.time()
for i in range(100):
    Task2.convert()
time_end = time.time()
print('Task2: ' + str(time_end - time_start))

time_start = time.time()
for i in range(100):
    Task3.convert()
time_end = time.time()
print('Task3: ' + str(time_end - time_start))