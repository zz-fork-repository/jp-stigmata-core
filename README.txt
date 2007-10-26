
                   Stigmata: Java birthmark toolkit
                            version 1.2.0
                   http://stigmata.sourceforge.jp/

                              Copyright 2006-2007 Haruaki Tamada, Ph.D
                                            Software Engineering Lab.,
                           Graduate School of Information and Science,
                             Nara Institute of Science and Technology,

* Overview

    Stigmata is Java birthmark toolkit, which aims to detect the
  theft of programs.  This tool can extract birthmarks from Java
  class files directory, and compare them.

    A birthmark is a set of special informations that the program
  originally possesses.  The birthmark is carefully extracted from
  critical portions of class file.  Hence, if a class file P has
  the same birthmark as another class file Q's, Q is very likely to
  be a copy of P.  Thus, the birthmark can be used as a simple but
  powerful signature to distinguish doubtful class files (those
  which seem to be copies).

* Features

    Stigmata support the extracting birthmarks from Java class
  files, and written in Java 5 with ASM.

  The main features are:

  - extraction of the four types of birthmarks directly from Java
    class files (without source code),
  - pairwise birthmark comparison of Java class files,
  - Jar file and War file support,
  - plug-in architecture for new birthmarks, and
  - analysis of extracted birthmarks (MDS)

* Requirements

  Stigmata requires following libraries.

  - ASM 2.2.3 (http://asm.objectweb.org/)
  - Jakarta Commons CLI 1.1 (http://commons.cafebabe.jp/xmlcli/)
  - XmlCli 1.2.1 (http://talisman.sourceforge.jp/xmlcli/)
  - Jama 1.0.2 (http://math.nist.gov/javanumerics/jama/)
    stigmata-1.0.0 or later
  - BrowserLauncher2 1.3 (http://browserlaunch2.sourceforge.net/)
    stigmata-1.1.0 or later; as needed
  - stax-api-1.0.1.jar (http://stax.codehaus.org/)
    stigmata-1.2.0 or later
  - stax-1.2.0.jar (http://stax.codehaus.org/)
    stigmata-1.2.0 or later
  - JUnit 4.1 (http://www.junit.org/) for testing.

* Author

  Name:        Haruaki TAMADA, Ph.D.
  Affiliation: Software Engineering Laboratory, Graduate School of
               Information and Science, Nara Institute of Science and
               Technology
  E-mail:      harua-t[ at ]is.naist.jp
  Web Page:    http://se.naist.jp/~harua-t/

  Please notify us some bugs and requests to 
  mailto:stigmata-info[ at ]lists.sourceforge.jp
  
