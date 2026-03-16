# Как запустить?
Поправить форматирование: 
``` docker run -it --rm -v "${PWD}:/app" -w /app ryukzak/wrench:latest wrench-fmt acc32.s --isa acc32 --inplace ```

Запустить в докере (предварительно скачав образ `ryukzak/wrench:latest`):
```docker run -it --rm -v "${PWD}:/app" -w /app ryukzak/wrench:latest wrench acc32.s --isa acc32 -c acc32.yaml -v```