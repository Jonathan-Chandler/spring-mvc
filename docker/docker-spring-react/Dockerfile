FROM archlinux

#VOLUME /home/jonathan/work/java/spring-mvc/spring-docker:/home/user

RUN pacman -Syu --noconfirm
# RUN useradd -m -G wheel -s /bin/bash jonathan
# RUN echo "%wheel	ALL = (ALL) NOPASSWD: ALL" >> /etc/sudoers

# language
RUN echo "en_US.UTF-8 UTF-8" > /etc/locale.gen
RUN locale-gen
RUN echo "LANG=en_US.UTF-8" > /etc/locale.conf

# react / mariadb / spring
EXPOSE 3000
#EXPOSE 3000 3306 8080

RUN pacman -Sy --noconfirm \
	jre11-openjdk \
	ant \
	maven \
	mariadb \
	npm

RUN mariadb-install-db --user=mysql --basedir=/usr --datadir=/var/lib/mysql
RUN useradd -m -s /bin/bash user
#RUN pacman -Sy --noconfirm \
#	chpasswd
RUN echo "user:pass" | chpasswd

RUN pacman -Sy --noconfirm \
	expect

# CMD ["bash", "-c", "/home/user/docker/docker-spring-react-sql/startup-mariadb.sh"]

