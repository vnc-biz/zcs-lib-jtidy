
TOPDIR=.
include $(TOPDIR)/conf.mk

DEBDIR=$(IMAGE_ROOT)/DEBIAN
DEBFILE=$(PACKAGE)_$(VERSION)_$(ARCHITECTURE).deb
ZMPKG?=zmpkg

all:	check-depend $(DEBFILE)

prepare:
	@echo -n > $(TOPDIR)/zimlets.list

build-scripts:
	@mkdir -p $(INSTALL_DIR)
	@if [ -f scripts/mailboxd-db-schema.sql ] ; then cp scripts/mailboxd-db-schema.sql $(INSTALL_DIR) ; fi
	@if [ -f scripts/post-install.sh        ] ; then \
		cp scripts/post-install.sh $(INSTALL_DIR) && chmod +x $(INSTALL_DIR)/post-install.sh ; fi

build-zimlets:	prepare
	@$(MAKE) -C src all

$(DEBFILE)::	$(DEBDIR)/control build-scripts build-zimlets
	@dpkg --build $(IMAGE_ROOT) .

$(DEBDIR)/control:	control.in
	@mkdir -p $(IMAGE_ROOT)/DEBIAN
ifeq ($(DEPENDS),)
	@cat $< | \
	    sed -E 's/@PACKAGE@/$(PACKAGE)/' | \
	    sed -E 's/@VERSION@/$(VERSION)/' | \
	    sed -E 's/@MAINTAINER@/$(MAINTAINER)/' | \
	    sed -E 's/@SECTION@/$(SECTION)/' | \
	    sed -E 's/@ARCHITECTURE@/$(ARCHITECTURE)/' | \
	    sed -E 's/@PRIORITY@/$(PRIORITY)/' | \
	    sed -E 's/@DEPENDS@/$(DEPENDS)/' | \
	    sed -E 's/@DESCRIPTION@/$(DESCRIPTION)/' | \
	    grep -vE "^Depends: " > $@
else
	@cat $< | \
	    sed -E 's/@PACKAGE@/$(PACKAGE)/' | \
	    sed -E 's/@VERSION@/$(VERSION)/' | \
	    sed -E 's/@MAINTAINER@/$(MAINTAINER)/' | \
	    sed -E 's/@SECTION@/$(SECTION)/' | \
	    sed -E 's/@ARCHITECTURE@/$(ARCHITECTURE)/' | \
	    sed -E 's/@PRIORITY@/$(PRIORITY)/' | \
	    sed -E 's/@DEPENDS@/$(DEPENDS)/' | \
	    sed -E 's/@DESCRIPTION@/$(DESCRIPTION)/' > $@
endif

clean:
	@$(MAKE) -C src clean
	@rm -Rf $(DISTPREFIX) $(IMAGE_ROOT) $(DEBFILE) zimlets.list

upload:	all
	@if [ ! "$(REDMINE_UPLOAD_USER)" ];     then echo "REDMINE_UPLOAD_USER environment variable must be set"     ; exit 1 ; fi
	@if [ ! "$(REDMINE_UPLOAD_PASSWORD)" ]; then echo "REDMINE_UPLOAD_PASSWORD environment variable must be set" ; exit 1 ; fi
	@if [ ! "$(REDMINE_UPLOAD_URL)" ];      then echo "REDMINE_UPLOAD_URL variable must be set"                  ; exit 1 ; fi
	@if [ ! "$(REDMINE_UPLOAD_PROJECT)" ];  then echo "REDMINE_UPLOAD_PROJECT variable must be set"              ; exit 1 ; fi
	@zm_redmine_upload			\
		-f "$(DEBFILE)"			\
		-l "$(REDMINE_UPLOAD_URL)"	\
		-u "$(REDMINE_UPLOAD_USER)"	\
		-w "$(REDMINE_UPLOAD_PASSWORD)"	\
		-p "$(REDMINE_UPLOAD_PROJECT)"	\
		-d "$(DEBFILE)"

check-depend:
	@zmpkg check-installed "$(DEPENDS)"
