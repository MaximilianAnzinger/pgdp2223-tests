# Tests for W13H02

**WARNING:** The tests only execute with a working simulator in W13H03 (solve W13H03 before using this tests)

1. Change `build.gradle` to:
```java
apply plugin: 'java'
sourceCompatibility = 17
version = '1.0.0'
compileJava.options.encoding = 'UTF-8'

repositories {
    mavenCentral()
}

def h03 = new File(System.getProperty("user.dir").replace("pgdp2223w13h02-", "pgdp2223w13h03-") + '/src')

def dirs = ['src']

if (h03.exists()) {
    dirs.add(h03.path)
} else {
    throw new FileNotFoundException("Couldn't find W13H03 at the expected location, please follow the steps in ".toUpperCase() + System.getProperty("user.dir") + "/test/pgdp/krypto/README.md")
}

sourceSets {
    main {
        java {
            srcDirs =  dirs
        }
    }
    test {
        java {
            srcDirs = ['test']
        }
    }
}

test {
    useJUnitPlatform()
}

dependencies {
    testImplementation('org.junit.jupiter:junit-jupiter:5.9.1')
}
```

2. Your files should be structured like this:
```
.
├── pgdp2223w13h02-xxnnxxx
│   ├── src
│   │   └── pgdp.krypto
│   │       ├── cryption.jvm
│   │       └── eea.jvm
│   │── test
│   │   └── pgdp.krypto
│   │       ├── cryptionTest.java
│   │       ├── eeaTest.java
│   │       └── README.md (this file)
│   └ build.gradle (the file you changed in step 1.)
├── pgdp2223w13h03-xxnnxxx
│   └── src
│       └── pgdp.pvm
│           ├── IO.java
│           ├── PVMParser.java
│           └── ...
```
**ONLY IF THIS IS NOT THE CASE**
3. Change this 


    System.getProperty("user.dir").replace("pgdp2223w13h02-", "pgdp2223w13h03-") + '/src'

to the absolute path of your w13h03 `src` folder