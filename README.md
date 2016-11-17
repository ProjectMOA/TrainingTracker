# Training Tracker

[![Build Status](https://travis-ci.org/UNIZAR-30248-2016-TrainingTracker/trainingTracker.svg?branch=master)](https://travis-ci.org/UNIZAR-30248-2016-TrainingTracker/trainingTracker)

## Start using this App

Assuming you already have [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) and [Gradle](https://docs.gradle.org/current/userguide/installation.html) installed, you must fork this repo. After that, you have to clone your forked repo on your local machine on the directory you desire: `git clone https://github.com/your-github-username/trainingTracker`.

You will also need to have installed a MySQL server running in localhost:3306 and a user called `travis` with empty password.

In order to check that everything is working well, you can execute the commands `gradle build` or `gradle check`.  

## Build & Run

The first time you deploy the app you will need to execute the deployDB.sql script running this in the project directory:
```
mysql -u travis < deployDB.sql
```

Once you have configure the data base you can run the application use the following commands:

- **gradle appRun** - Deploys a Jetty server in http://localhost:8080, using the files located on the project's source directory.
This allows to modify any of these files and be able to see the changes in hot. Beware! If we modify any .java file, 
we will hace to compile them again on another terminal with 'gradle classes'.

- **gradle appRunWar** - Deploys a Jetty server in http://localhost:8080, using the .war file generated by the project. With this option
we won't be able to see any modification in hot, but in exachange it'll allow us to test the .war after the compiling and 
packaging process.

- **To rebuild** the web-app and then restart the web-server you need to press any keypress excluding 'q' or 'Q'.

- **To stop** the server you you need to press 'q' or 'Q'.

You can also other [Gretty tasks](http://akhikhl.github.io/gretty-doc/Gretty-tasks) for running and debugging the web-app.

## Tests

Gradle allows you to run the tests defined on src/test with `gradle test`.

In order to run the Selenium test successfully, you'll have to run the app first in another terminal. To do so, you 
can execute one of the previously mentioned commands in the "Build & Run" section.

Besides, you'll have to install Firefox 47.0.1 or lower in order to run the tests due to Selenium WebDriver restrictions.

Note that `gradle check`and `gradle build` will also execute the tests, so remember to perform the previos steps.

You can indicate gradle no to run them with the pervios mentioned commands using the `-x test` option, i.e `gralde build -x test`.

Another way to prevent the test from being executed is to add the "@Ignore" annotation before the test class declaration.

# EditorConfig 
[EditorConfig](http://editorconfig.org/) helps developers maintain consistent coding styles between different editors and IDEs. It is a file format for defining coding styles and a collection of text editor plugins that enable editors to read the file format and adhere to defined styles.
You need to create a .editorconfig file in which you define the coding style rules. It is similar to the format accepted by gitignore.

## IDEs supported by EditorConfig
These editors come bundled with native support for EditorConfig. Everything should just work: [BBEdit](http://www.barebones.com/support/technotes/editorconfig.html), [Builder](https://wiki.gnome.org/Apps/Builder/Features#EditorConfig), [CLion](https://github.com/JetBrains/intellij-community/tree/master/plugins/editorconfig), [GitHub](https://github.com/RReverser/github-editorconfig#readme), [Gogs](https://gogs.io/), [IntelliJIDEA](https://github.com/JetBrains/intellij-community/tree/master/plugins/editorconfig), [RubyMine](https://github.com/JetBrains/intellij-community/tree/master/plugins/editorconfig), [SourceLair](https://www.sourcelair.com/features/editorconfig), [TortoiseGit](https://tortoisegit.org/), [WebStorm](https://github.com/JetBrains/intellij-community/tree/master/plugins/editorconfig).

## IDEs not supported by EditorConfig file

To use EditorConfig with one of these editors, you will need to install a plugin: [AppCode](https://plugins.jetbrains.com/plugin/7294), [Atom](https://github.com/sindresorhus/atom-editorconfig#readme), [Brackets](https://github.com/kidwm/brackets-editorconfig/), [Coda](https://panic.com/coda/plugins.php#Plugins), [Code::Blocks](https://github.com/editorconfig/editorconfig-codeblocks#readme), [Eclipse](https://github.com/ncjones/editorconfig-eclipse#readme), [Emacs](https://github.com/editorconfig/editorconfig-emacs#readme), [Geany](https://github.com/editorconfig/editorconfig-geany#readme), [Gedit](https://github.com/editorconfig/editorconfig-gedit#readme), [Jedit](https://github.com/editorconfig/editorconfig-jedit#readme), [Komodo](http://komodoide.com/packages/addons/editorconfig/), [NetBeans](https://github.com/welovecoding/editorconfig-netbeans#readme), [NotePadd++](https://github.com/editorconfig/editorconfig-notepad-plus-plus#readme), [PhpStorm](https://plugins.jetbrains.com/plugin/7294), [PyCharm](https://plugins.jetbrains.com/plugin/7294), [Sublime Text](https://github.com/sindresorhus/editorconfig-sublime#readme), [Textadept](https://github.com/editorconfig/editorconfig-textadept#readme), [textmate](https://github.com/Mr0grog/editorconfig-textmate#readme), [Vim](https://github.com/editorconfig/editorconfig-vim#readme), [Visual Studio](https://github.com/editorconfig/editorconfig-visualstudio#readme), [Visual Studio Code](https://marketplace.visualstudio.com/items?itemName=EditorConfig.EditorConfig), [Xcode](https://github.com/MarcoSero/EditorConfig-Xcode)
