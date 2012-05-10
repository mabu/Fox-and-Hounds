PACKAGES = \
	foxandhounds.logic \
	foxandhounds.gui \

NODOC_PACKAGES = \

JARS = \
        foxandhounds.jar

JARS_3RDPARTY = \

MAIN_CLASS     = Main
MAIN_PACKAGE   = foxandhounds.gui
MAIN_JAR       = foxandhounds.jar

RUN_PARAMETERS = 


#*********************************************************************
#
# Javadoc
#
#*********************************************************************

WINDOWTITLE = ''
DOCTITLE    = ''
HEADER      = ''
BOTTOM      = ''

include $(JAVA_DEV_ROOT)/make/Makefile
