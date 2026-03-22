#Mapeo fisico de la base de datos de la API

#Autenticacion de usuario en sql
mysql -u admindba -p 

#Creacion de la base de datos

CREATE DATABASE pkmformsdb;

#Acceder hacia la base de datos desde la terminal
USE pkmformsdb;


#Tabla para poder gestionar los formularios subidos

CREATE TABLE formulario(
    id INT NOT NULL AUTO_INCREMENT,
    
    autor VARCHAR(250) NOT NULL,
    
    filename VARCHAR(150) NOT NULL,

    fecha_publicacion DATE DEFAULT (CURRENT_DATE),
    
    hora_publicacion TIME DEFAULT (CURRENT_TIME),
    
    archivo LONGBLOB NOT NULL,

    CONSTRAINT pk_formulario PRIMARY KEY (id)
);
