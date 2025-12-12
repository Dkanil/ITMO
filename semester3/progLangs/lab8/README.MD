# Лабораторная работа 8
## Задание
1. Запустить примеры
2. Написать класс (у меня KnightMechanics), на которой повесить атрибут GameAttribute 
3. Создать два метода в этом классе с атрибутами CombatSkillAttribute с тригерами (OnDefense и на PostBattle)
	а) OnDefense снижение урона в два раза
	б) PostBattle у Attacker и Defender снижается HP на 10
4. Вызвать попорядку pipeline: OnDefense => OnAttack => PostBattle
5. 3.а и 3.б пункты: сделать математику с помощью ILGenerator

## Как запустить
```dotnet run```