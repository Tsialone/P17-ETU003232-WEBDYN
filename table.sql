CREATE TABLE Prevision (
    id INT  PRIMARY KEY,
    libelle VARCHAR(255) NOT NULL,
    montant DOUBLE NOT NULL
);

CREATE TABLE Depense (
    id INT PRIMARY KEY,
    idPrevision INT,
    montant DOUBLE ,
    FOREIGN KEY (idPrevision) REFERENCES Prevision(id)
);

CREATE TABLE Etat (
    id INT PRIMARY KEY ,
    idPrevision INT , 
    depense DOUBLE , 
    reste DOUBLE 
);

