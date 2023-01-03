# Contributing

You want to contribute and add tests to this repository? Great! We are happy to
accept your contributions. Please read the following guidelines before you
start.

## Guidelines for contributing

**TL;DR**

* PR title and description should use a prefix like `[W09H03] Add behavioral tests`
* Separate your tests logically into small files
* Don't add tests that are not needed for the exercise
* Write deterministic tests
* Use JUnit 5.9.1
* Follow the Java Style Guide
* Don't depend on changing the code of the implementation
* Every test file should end with `Test.java`
* Follow the package structure of the implementation
* Only one test case per test method
* Use descriptive names for test methods.

### PR Title and Description

#### Title

Please as prefix for your PR title the name of the exercise you are working on.

```
[W09H03] Add behavioral tests
```

If you don't changed the tests for an exercise, you can use `[*]` as prefix:

```
[*] ...
```

#### Description

The description of your PR should help us understand what you are doing: For example, when fixing a bug, provide information on the bug and a brief sentence on how you fixed it.

### Separate your tests logically into small files

Please don't add all your tests into one file. Instead, please create one file
per test class. For example, you can create one test for every sub task.

### Don't add tests that are not needed for the exercise

Please don't add tests that are not needed for the exercise. For example, if the
exercise only requires you to implement a method `foo(int a)` and the exercise
says that `a` is never negative, you don't need to test if `a` is negative.

### Write deterministic tests

Please make sure that your tests are deterministic. For example, if you test a
method that returns a random number, you should use a fixed seed for the
random number generator.

**BAD**:
```java
Random random = new Random();
```

**GOOD**:
```java
// Use a fixed seed for the random number generator
Random random = new Random(42);
```

### Use JUnit 5.9.1

We use JUnit 5.9.1 for testing. You can find the documentation
[here](https://junit.org/junit5/docs/current/user-guide/).

To use JUnit 5.9.1 in your project, you need to add the following dependency to your `build.gradle`.

```gradle
dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter:5.9.1'
}
```

### Format your code

Make sure that your code is formatted according to the [Google Java Style
Guide](https://google.github.io/styleguide/javaguide.html).

### Don't depend on changing the code of the implementation

The test should not say "you need to change the class `Foo` from `private` to `public`".

In case you need to test a private method, you can use
[Reflection](https://docs.oracle.com/javase/tutorial/reflect/index.html).

### Every test file should end with `Test.java`

**BAD**
```
ReadWayPoints.java
```

**GOOD**
```
ReadWayPointsTest.java
```

### Follow the package structure of the implementation

You should copy the package structure of the implementation to the test.

**BAD**
```java
// Location of the implementation
src/pgdp/pingu/ReadWayPoints.java

// Location of the test file
test/pingu/ReadWayPointsTest.java
```

**GOOD**
```java
// Location of the implementation
src/pgdp/pingu/ReadWayPoints.java

// Location of the test file
test/pgdp/pingu/ReadWayPointsTest.java
```

### Only one test case per test method

**BAD**
    
```java
@Test
public void testAddition() {
  assertEquals(2, 1 + 1);
  assertEquals(0, 0 + 0);
  assertEquals(-2, 5 + -7);
}
```

**GOOD**
    
```java
@Test
public void testAdditionPositive() {
  assertEquals(2, 1 + 1);
}

@Test
public void testAdditionZero() {
  assertEquals(0, 0 + 0);
}

@Test
public void testAdditionNegative() {
  assertEquals(-2, 5 + -7);
}
```

### Use descriptive names for test methods.

**BAD**:
```java
@Test
public void test1() {
  // ...
}

@Test
public void test2() {
  // ...
}

@Test
public void test3() {
  // ...
}
```


**GOOD**:

```java
@Test
public void testEmptyList() {
  // ...
}

@Test
public void testSingleElementList() {
  // ...
}

@Test
public void testMultipleElementList() {
  // ...
}
```

### Make sure you set `expected` and `actual` correctly.

The `assertEquals` (and other) methods have the following signature:

```java
assertEquals(expected, actual);
```

**BAD**:
```java
int expected = 2;
int actual = 1 + 1;

assertEquals(actual, expected);
```

**GOOD**:
```java
int expected = 2;
int actual = 1 + 1;

assertEquals(expected, actual);
```

### Use English as language

Please use English as language for your test methods and comments.
