
include $(TOPDIR)/conf.mk

ANT?=ant
IMPORT_CP=`[ -d lib ] && find lib -name "*.jar" -exec "echo" "-n" "{}:" ";"`
SRCS=`find -L src -name "*.java"`

ZIMLET_USER_JARDIR=mailboxd/webapps/zimbra/WEB-INF/lib
ZIMLET_ADMIN_JARDIR=mailboxd/webapps/zimbraAdmin/WEB-INF/lib
ZIMLET_SERVICE_JARDIR=mailboxd/webapps/service/WEB-INF/lib
ZIMLET_LIB_JARDIR=lib/jars

all:	build

build:	check build_ant install

check:
ifeq ($(BUILD_ANT_SUBDIR),)
	@echo "missing $(BUILD_ANT_SUBDIR)" >&2
	@exit 1
endif

install:	build_ant
ifeq ($(INSTALL_USER),y)
	@mkdir -p $(IMAGE_ROOT)/$(ZIMLET_USER_JARDIR)
	@cp $(BUILD_ANT_JARFILE) $(IMAGE_ROOT)/$(ZIMLET_USER_JARDIR)
endif
ifeq ($(INSTALL_ADMIN),y)
	@mkdir -p $(IMAGE_ROOT)/$(ZIMLET_ADMIN_JARDIR)
	@cp $(BUILD_ANT_JARFILE) $(IMAGE_ROOT)/$(ZIMLET_ADMIN_JARDIR)
endif
ifeq ($(INSTALL_SERVICE),y)
	@mkdir -p $(IMAGE_ROOT)/$(ZIMLET_SERVICE_JARDIR)
	@cp $(BUILD_ANT_JARFILE) $(IMAGE_ROOT)/$(ZIMLET_SERVICE_JARDIR)
endif
ifeq ($(INSTALL_LIB),y)
	@mkdir -p $(IMAGE_ROOT)/$(ZIMLET_LIB_JARDIR)
	@cp $(BUILD_ANT_JARFILE) $(IMAGE_ROOT)/$(ZIMLET_LIB_JARDIR)
endif

clean:
	@cd $(BUILD_ANT_SUBDIR) && $(ANT) clean
	@rm -Rf \
		classes		\
		$(IMAGE_ROOT)/$(ZIMLET_SERVICE_JARDIR)/$(BUILD_ANT_JARFILE)	\
		$(IMAGE_ROOT)/$(ZIMLET_ADMIN_JARDIR)/$(BUILD_ANT_JARFILE)	\
		$(IMAGE_ROOT)/$(ZIMLET_USER_JARDIR)/$(BUILD_ANT_JARFILE)

build_ant:
	@cd $(BUILD_ANT_SUBDIR) && $(ANT) $(BUILD_ANT_TARGET)
