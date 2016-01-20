create table Client (
	numClient number(3),
	mail varchar2(20) not null,
	nom varchar2(20) not null,
	prenom varchar2(20) not null,
	motDePasse varchar2(20) not null,
	adresse varchar2(20) not null,
	actif number(1),
	constraint Client_C1 primary key (NumClient)
);
