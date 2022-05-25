

# What is buildSrc ?

- composite build
- buildSrc build 和 main build 是分离的

1. 不可分享构建逻辑
2. 目的是为了在这里自定义 plugin 和 task 给其他 module 用
3. dependencies 会传递
4. buildScript 不会传递

