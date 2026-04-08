# Как запустить?
Поправить форматирование: 
- ``` docker run -it --rm -v "${PWD}:/app" -w /app ryukzak/wrench:latest wrench-fmt task.s --isa acc32 --inplace ```

Запустить в докере (предварительно скачав образ `ryukzak/wrench:latest`):
- ```docker run -it --rm -v "${PWD}:/app" -w /app ryukzak/wrench:latest wrench task.s --isa acc32 -c test1.yaml -v```