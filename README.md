# Devoxx 2013

This is the content for the talk, _Designing a REST-ful API Using Spring 4_ for the [Devoxx 2013 conference][].  The contents of this repository include the slides, speaker's notes, designs, and final code for the talk.

## Requirements

### Java, Maven
The application is written in Java and packaged as a self-executable JAR file.  This enables it to run anywhere that Java is available.  Building the application (required for deployment) requires [Maven][].

### Ruby, Bundler
The client demo application is written in Ruby and can run wherever Ruby 1.9.3 or later is available.  It's dependencies are managed using the [Bundler gem][].

## Developing
The project is set up as a Maven project and doesn't have any special requirements beyond that.  In order to build and run the application execute the following commands:

```bash
mvn package
java -jar target/lets-make-a-deal.jar
```

In order to run the client application execute the following commands:

```bash
bundle install
bundle exec ./client.rb
```

## License
The project is released under version 2.0 of the [Apache License][].

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
[Bundler gem]: http://bundler.io
[Devoxx 2013 conference]: http://www.devoxx.be
[Maven]: http://maven.apache.org
