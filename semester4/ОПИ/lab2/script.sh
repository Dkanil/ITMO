mkdir src
cd src
git init
git branch -M main
unzip ../commits/commit0.zip
git add .
git commit -m "init commit r0"

git checkout -b blue1
unzip -o ../commits/commit1.zip
git add .
git commit --author "blue <blue@example.com>" -m "make branch blue1 (r1)"

git checkout -b blue2
unzip -o ../commits/commit2.zip
git add .
git commit --author "blue <blue@example.com>" -m "make branch blue2 (r2)"
unzip -o ../commits/commit3.zip
git add .
git commit --author "blue <blue@example.com>" -m "r3"

git checkout blue1
unzip -o ../commits/commit4.zip
git add .
git commit --author "blue <blue@example.com>" -m "r4"

git checkout main
unzip -o ../commits/commit5.zip
git add .
git commit -m "r5"
unzip -o ../commits/commit6.zip
git add .
git commit -m "r6"
unzip -o ../commits/commit7.zip
git add .
git commit -m "r7"
git checkout main
unzip -o ../commits/commit8.zip
git add .
git commit -m "r8"

git checkout blue1
unzip -o ../commits/commit9.zip
git add .
git commit --author "blue <blue@example.com>" -m "r9"
unzip -o ../commits/commit10.zip
git add .
git commit --author "blue <blue@example.com>" -m "r10"

git checkout main
git merge blue1 --no-commit
unzip -o ../commits/commit11.zip
git add .
git commit -m "Merge branch 'blue1' into 'main' (r11)"

git checkout blue2
unzip -o ../commits/commit12.zip
git add .
git commit --author "blue <blue@example.com>" -m "r12"

git checkout main
git merge blue2 --no-commit
unzip -o ../commits/commit13.zip
git add .
git commit --author "blue <blue@example.com>" -m "Merge branch 'blue2' into 'main' (r13)"
unzip -o ../commits/commit14.zip
git add .
git commit -m "r14"

git log --graph --oneline

