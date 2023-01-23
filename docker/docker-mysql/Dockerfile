FROM archlinux

RUN pacman -Syu --noconfirm

# language
RUN echo "en_US.UTF-8 UTF-8" > /etc/locale.gen
RUN locale-gen
RUN echo "LANG=en_US.UTF-8" > /etc/locale.conf

# mariadb
EXPOSE 3306

RUN pacman -Sy --noconfirm \
	jre11-openjdk \
	mariadb

RUN mariadb-install-db --user=mysql --basedir=/usr --datadir=/var/lib/mysql
RUN mkdir /home/mysql

#RUN pacman -Sy --noconfirm \
#	chpasswd
#RUN echo "user:pass" | chpasswd

RUN pacman -Sy --noconfirm \
	expect

ENTRYPOINT ["bash", "-c", "/home/mysql/startup-mariadb.sh"]
