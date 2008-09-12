
                   Stigmata: Java birthmark toolkit
                            version 2.0.0
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
  - Apache Commons DBUtils 1.1 (http://commons.apache.org/dbutils/)
  - Apache Commons Beanutils 1.7.0 (http://commons.apache.org/beanutils/)
  - Talisman XmlCli 1.2.2 (http://talisman.sourceforge.jp/xmlcli/)
  - Talisman MDS 1.0.1 (http://talisman.sourceforge.jp/mds/)
  - Talisman i18n 1.0.1 (http://talisman.sourceforge.jp/i18n/)
  - Stigmata Digger 1.0.0 (http://stigmata.sourceforge.jp/digger/)

  - JUnit 4.1 (http://www.junit.org/) for testing.

* Author

  Name:        Haruaki TAMADA.
  Affiliation: Stigmata Project, Sourceforge.jp
  E-mail:      tama3[ at ]users.sourceforge.jp
  Web Page:    http://stigmata.sourceforge.jp/

  Please notify us some bugs and requests to 
  mailto:stigmata-info[ at ]lists.sourceforge.jp
  
