# AI智能答题平台

1.要懂得画流程图

## 第二期 小程序开发

### 1.MBTI实现方案

#### 1.题目结构

json格式的,更加的灵活,适合排序缺点是占用空间

#### 2.用户答案结构

直接有选项组成就可以了

优点: 不用再完整传递题目的结构，节省传输体积，提高性能。

```json
["A","A","A","B","A","A","A","B","B","A"]
```

#### 3.评分规则

就是通过给每一个答案设置属性,然后对答案进行遍历算出属性的分数,对应参考答案的比例选出最合适的结果

### 2.Taro初步开发

学会了基本的taro开发,可以看官方文档初始化,

查了nvm切换版本

用了微信开发者工具

ts看懂一点!

### 3.小程序开发常用解决方案

#### 1.网络请求

主流的是 `Axios`

文档: [The Axios Instance | Axios Docs (axios-http.com)](https://axios-http.com/docs/instance)

## 第三期 平台-后端开发

### 1.需求分析:

先做出我们想要做的基本功能,然后完善更细的功能

**我们得对功能进行分级, 分清楚优先级!!!!**

### 2.库表设计

**设定要用的基本表,表中的基本字段是必不可少的,比如时间三件套**

为什么要有冗余字段?

因为回答记录一旦设置，几乎不会更改，便于查询，不用联表，节约开发成本。

### 3. 后端核心业务流程开发

### 1.审核业务

通用写一个reviewRequest,然后审核!!

步骤:

1. 首先是管理员
2. 判断是否存在
3. 判断他的状态然后更新即可

### 2.评分模块

运用策略模式,定一个总的接口

新的评分模式就实现这个接口

比如自定义评分,自定义测试等评分模块

**怎么运用呢?**

定义一个工具类 管理所有的策略

1. 引入所有的评分策略,然后执行方法的时候Switch()判断他的app类型和评分策略,**优点是 直观清晰,缺点是不利于扩展和维护**
2. 定义一个注解给每种策略加上,然后遍历策略判断是否相等执行评分方法, **优点是扩展方便,不要修改代码**

## 第四期 前端开发

## 第五期 平台智能化

调用智普ai,首先就是要看文档!!然后创建单元测试一下

#### `引入AI模块:`

1.首先导入pom包

2.创建config类注册调用`AI的客户端(client)`

3.然后建立一个`manager`,封装通用的方法,提供不同参数默认值的简化调用方法

#### `生成题目功能`

AI生成内容的核心是编写 Prompt(指导), 需要精准!!

我们首先要明确输入给AI的参数,然后构建Prompt输入给AI

**输入参数:**

1.应用信息: 应用名称, 描述, 类别 (得分/测评)

2.题目信息: 题数, 每题选项数

~~~json
你是一位严谨的出题专家，我会给你如下信息：
```
应用名称，
【【【应用描述】】】，
应用类别，
要生成的题目数，
每个题目的选项数
```

请你根据上述信息，按照以下步骤来出题：
1. 要求：题目和选项尽可能地短，题目不要包含序号，每题的选项数以我提供的为主，题目不能重复
2. 严格按照下面的 json 格式输出题目和选项
```
[{"options":[{"value":"选项内容","key":"A"},{"value":"","key":"B"}],"title":"题目标题"}]
```
title 是题目，options 是选项，每个选项的 key 按照英文字母序（比如 A、B、C、D）以此类推，value 是选项内容
3. 检查题目是否包含序号，若包含序号则去除序号
4. 返回的题目列表格式必须为 JSON 数组

~~~



![image-20250125232904548](C:\Users\16247\AppData\Roaming\Typora\typora-user-images\image-20250125232904548.png)

##### `后端开发`

1.首先建立AI生成题目请求类(AppId, 题目数量,题目选项)

2.定义模版常量和构造用户模版的方法:

```java
private static final String GENERATE_QUESTION_SYSTEM_MESSAGE = "你是一位严谨的出题专家，我会给你如下信息：\n" +
        "```\n" +
        "应用名称，\n" +
        "【【【应用描述】】】，\n" +
        "应用类别，\n" +
        "要生成的题目数，\n" +
        "每个题目的选项数\n" +
        "```\n" +
        "\n" +
        "请你根据上述信息，按照以下步骤来出题：\n" +
        "1. 要求：题目和选项尽可能地短，题目不要包含序号，每题的选项数以我提供的为主，题目不能重复\n" +
        "2. 严格按照下面的 json 格式输出题目和选项\n" +
        "```\n" +
        "[{\"options\":[{\"value\":\"选项内容\",\"key\":\"A\"},{\"value\":\"\",\"key\":\"B\"}],\"title\":\"题目标题\"}]\n" +
        "```\n" +
        "title 是题目，options 是选项，每个选项的 key 按照英文字母序（比如 A、B、C、D）以此类推，value 是选项内容\n" +
        "3. 检查题目是否包含序号，若包含序号则去除序号\n" +
        "4. 返回的题目列表格式必须为 JSON 数组";

private String getGenerateQuestionUserMessage(App app, int questionNumber, int optionNumber) {
    StringBuilder userMessage = new StringBuilder();
    userMessage.append(app.getAppName()).append("\n");
    userMessage.append(app.getAppDesc()).append("\n");
    userMessage.append(AppTypeEnum.getEnumByValue(app.getAppType()).getText() + "类").append("\n");
    userMessage.append(questionNumber).append("\n");
    userMessage.append(optionNumber);
    return userMessage.toString();
}
//使用StringBuilder 构建
```

3.AI生成接口:

```java
@PostMapping("/ai_generate")
public BaseResponse<List<QuestionContentDTO>> aiGenerateQuestion(@RequestBody AiGenerateQuestionRequest aiGenerateQuestionRequest) {
    ThrowUtils.throwIf(aiGenerateQuestionRequest == null, ErrorCode.PARAMS_ERROR);
    // 获取参数
    Long appId = aiGenerateQuestionRequest.getAppId();
    int questionNumber = aiGenerateQuestionRequest.getQuestionNumber();
    int optionNumber = aiGenerateQuestionRequest.getOptionNumber();
    App app = appService.getById(appId);
    ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
    // 封装 Prompt
    String userMessage = getGenerateQuestionUserMessage(app, questionNumber, optionNumber);
    // AI 生成
    String result = aiManager.doSyncUnstableRequest(GENERATE_QUESTION_SYSTEM_MESSAGE, userMessage);
    // 结果处理
    int start = result.indexOf("[");
    int end = result.lastIndexOf("]");
    String json = result.substring(start, end + 1);
    List<QuestionContentDTO> questionContentDTOList = JSONUtil.toList(json, QuestionContentDTO.class);
    return ResultUtils.success(questionContentDTOList);
}

```

#### `AI 智能评分`

自己设置得分规则比较麻烦, 可以使用AI 根据应用信息,题目和用户答案进行评分,直接返回结果

**比较适合测评类应用**

**输入参数**:

1.应用信息

2.题目信息

3.用户答案

**编写的Prompt:**

~~~json
你是一位严谨的判题专家，我会给你如下信息：
```
应用名称，
【【【应用描述】】】，
题目和用户回答的列表：格式为 [{"title": "题目","answer": "用户回答"}]
```

请你根据上述信息，按照以下步骤来对用户进行评价：
1. 要求：需要给出一个明确的评价结果，包括评价名称（尽量简短）和评价描述（尽量详细，大于 200 字）
2. 严格按照下面的 json 格式输出评价名称和评价描述
```
{"resultName": "评价名称", "resultDesc": "评价描述"}
```
3. 返回格式必须为 JSON 对象

~~~

##### `后端开发`

1.编写题目答案封装类

```java
public class QuestionAnswerDTO {

    /**
     * 题目
     */
    private String title;

    /**
     * 用户答案
     */
    private String userAnswer;
}

```

2.编写AI测评类应用评分策略类,需要指定对应的注解

```java
@ScoringStrategyConfig(appType = 1, scoringStrategy = 1)

```

定义模版常量和构造用户模版的方法:

```java
private static final String AI_TEST_SCORING_SYSTEM_MESSAGE = "你是一位严谨的判题专家，我会给你如下信息：\n" +
        "```\n" +
        "应用名称，\n" +
        "【【【应用描述】】】，\n" +
        "题目和用户回答的列表：格式为 [{\"title\": \"题目\",\"answer\": \"用户回答\"}]\n" +
        "```\n" +
        "\n" +
        "请你根据上述信息，按照以下步骤来对用户进行评价：\n" +
        "1. 要求：需要给出一个明确的评价结果，包括评价名称（尽量简短）和评价描述（尽量详细，大于 200 字）\n" +
        "2. 严格按照下面的 json 格式输出评价名称和评价描述\n" +
        "```\n" +
        "{\"resultName\": \"评价名称\", \"resultDesc\": \"评价描述\"}\n" +
        "```\n" +
        "3. 返回格式必须为 JSON 对象";

private String getAiTestScoringUserMessage(App app, List<QuestionContentDTO> questionContentDTOList, List<String> choices) {
    StringBuilder userMessage = new StringBuilder();
    userMessage.append(app.getAppName()).append("\n");
    userMessage.append(app.getAppDesc()).append("\n");
    List<QuestionAnswerDTO> questionAnswerDTOList = new ArrayList<>();
    for (int i = 0; i < questionContentDTOList.size(); i++) {
        QuestionAnswerDTO questionAnswerDTO = new QuestionAnswerDTO();
        questionAnswerDTO.setTitle(questionContentDTOList.get(i).getTitle());
        questionAnswerDTO.setUserAnswer(choices.get(i));
        questionAnswerDTOList.add(questionAnswerDTO);
    }
    userMessage.append(JSONUtil.toJsonStr(questionAnswerDTOList));
    return userMessage.toString();
}

```

实现应用评分策略:

```java
@Override
public UserAnswer doScore(List<String> choices, App app) throws Exception {
    Long appId = app.getId();
    // 1. 根据 id 查询到题目
    Question question = questionService.getOne(
            Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, appId)
    );
    QuestionVO questionVO = QuestionVO.objToVo(question);
    List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();
    // 2. 调用 AI 获取结果
    // 封装 Prompt
    String userMessage = getAiTestScoringUserMessage(app, questionContent, choices);
    // AI 生成
    String result = aiManager.doSyncStableRequest(AI_TEST_SCORING_SYSTEM_MESSAGE, userMessage);
    // 结果处理
    int start = result.indexOf("{");
    int end = result.lastIndexOf("}");
    String json = result.substring(start, end + 1);
    // 3. 构造返回值，填充答案对象的属性
    UserAnswer userAnswer = JSONUtil.toBean(json, UserAnswer.class);
    userAnswer.setAppId(appId);
    userAnswer.setAppType(app.getAppType());
    userAnswer.setScoringStrategy(app.getScoringStrategy());
    userAnswer.setChoices(JSONUtil.toJsonStr(choices));
    return userAnswer;
}

```



## 第六期 性能优化

### 1.AI生成题目优化

### 2.AI 答题优化

对答题结果使用缓存,使用caffeine本地缓存

为了防止缓存击穿,使用锁来保护,抢到锁然后保存缓存后面的请求就可以利用缓存了

### 3.分库分表

sharding JDBC 已实现

### 4.app搜索优化

根据app描述来搜索!!

添加ES来实现

**步骤**:

1.建立es表()

2.建立EsDao(),自己写方法名,具体代码自动生成,(适合简单查询)

复杂查询用Spring提供的操作es的客户端对象

### 5.AppIcon实现上传功能

后端使用minio实现upload功能

前端使用uploader组件时，需要有回调函数，我们先得等图片上传完成，然后file作为参数传到后端执行upload函数

## 第七期 系统优化

### 1.幂等性

查看用户答案的时候可以连续点击几次,为了防止连续调用AI可以使用幂等性来保存

1.数据库唯一索引

创建`唯一索引ID`即可

2.分布式锁

使用Redisson来实现

### 2.线程池隔离

