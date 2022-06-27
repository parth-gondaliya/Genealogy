-- person table
CREATE TABLE Person (
    PersonId int NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
	PRIMARY KEY (PersonId) 
);


-- person_attributes table
CREATE TABLE Person_attributes (
    PersonId int,
    FOREIGN KEY (PersonId) REFERENCES person(PersonId),
    keyOfAttribute varchar(255),
    valueOfAttribute  varchar(255)
);
 
-- person reference
CREATE TABLE Person_reference (
    PersonId int NOT NULL,
    refer varchar(255) NOT NULL,
    FOREIGN KEY (PersonID) REFERENCES Person(PersonID)
);

-- person notes
CREATE TABLE person_note (
    PersonId int NOT NULL,
    Note varchar(255) NOT NULL,
    FOREIGN KEY (PersonID) REFERENCES person(PersonID)
);

-- parent child 
CREATE TABLE parent (
	Id int not null auto_increment ,
    ParentId int NOT NULL,
	FOREIGN KEY (ParentId) REFERENCES person(PersonId),
	ChildId int NOT NULL,
    FOREIGN KEY (ChildId) REFERENCES Person(PersonId),
	PRIMARY KEY (Id)
);

-- partner1 and partner2
CREATE TABLE marriage (
	Id int not null auto_increment ,
    partner1 int NOT NULL,
    FOREIGN KEY (partner1) REFERENCES person(PersonId),
    partner2 int NOT NULL,
    FOREIGN KEY (partner2) REFERENCES person(PersonId),
	PRIMARY KEY (Id)
); 

-- partner1 and partner2
CREATE TABLE divorce (
	Id int not null auto_increment,
	partner1 int NOT NULL,
	FOREIGN KEY (partner1) REFERENCES Person(PersonId),
    partner2 int NOT NULL,
    FOREIGN KEY (partner2) REFERENCES Person(PersonId),
	PRIMARY KEY (Id)
);


-- all decendents from parent - child table

WITH RECURSIVE descendant AS (
    SELECT  ParentId,
            ChildId,
            0 as level
    FROM parent
    WHERE ParentId = 87
 
    UNION ALL
 
    SELECT  p.ChildId,
            p.parentId,
            level + 1
    FROM parent p
JOIN descendant d
ON p.ParentId = d.ParentId
)
 
SELECT  d.ChildId AS descendant_id,
        p1.ChildId AS ancestor_id,
        d.level
FROM descendant d
JOIN parent p1
ON d.parentId = p1.ChildId
group by d.ParentId
having level  <=2
ORDER BY level;


-- ancetor from parent and child relationship
WITH RECURSIVE ancestor AS (
    SELECT  ChildId,
            parentId,
            1 AS level
    FROM parent
    WHERE ChildId = 22	
 
    UNION ALL
 
    SELECT  per.ChildId,
            per.parentId,
            level + 1
    FROM parent per
JOIN ancestor d
ON per.ChildId = d.parentId
)
 
SELECT  
        a.parentId AS ancestor_id,
        d.level
FROM ancestor d
JOIN parent a
ON d.ChildId = a.ChildId
where level <=5
group by  ancestor_id,level;

-- path from root to node 
WITH recursive person1 (PersonId, name, ParentId) AS (
	select PersonId,name,p.ParentId from person 
	left join parent as p 
	on person.personId = p.ChildId
),
category_path (PersonId, name, path) AS
(
  SELECT PersonId, name , CONCAT(PersonId) AS path
    FROM person1
    WHERE ParentId IS NULL
  UNION ALL
  SELECT c.PersonId, c.name,  CONCAT( c.PersonId, '-', cp.path )
    FROM category_path AS cp JOIN person1 AS c 	
      ON cp.PersonId = c.ParentId
)
SELECT * FROM category_path
where  personId=84 OR personId= 91
ORDER BY path;

-- file table 
CREATE TABLE files (
	fileId int not null auto_increment,
    fileLocation varchar(250) not null,
    PRIMARY KEY (fileLocation) ,
    date varchar(50),
    location varchar(255),
    key(fileId)
);

-- file attributes 
CREATE TABLE file_attributes (
    fileId int,
    FOREIGN KEY (fileId) REFERENCES files(fileId),
    keyOfAttribute varchar(255) not null,
    valueOfAttribute varchar(255)
);


-- file name with tags
create table tags(
	fileId int not null auto_increment,
	foreign key (fileId) references files(fileId),
    tag varchar(250)
);

-- file with including persons
create table fileWithPerson(
	fileId int not null ,
    foreign key (fileId) references files(fileId),
	PersonId int NOT NULL ,
    FOREIGN KEY (PersonID) REFERENCES Person(PersonID)
);

-- find a file using tag with dates 
SELECT f.fileId,tag,date as d FROM tags
INNER JOIN files as f
on tags.fileId=f.fileId
group by f.fileId,tag
having tag="asd" and ((DATE(STR_TO_DATE(date,'%Y-%m-%d')) between DATE(STR_TO_DATE('1985-12-30','%Y-%m-%d')) and  DATE(STR_TO_DATE('2020-12-30','%Y-%m-%d'))) or (d is null));

-- find biologica family media 
SELECT fp.fileId as fId, f.date as date,f.fileLocation as name FROM parent as p
join filewithperson as fp
on p.ChildId =  fp.PersonId and p.ParentId=14
join files as f 
on f.fileId = fp.fileId and f.date is not null
group by f.fileId
order by f.date,f.fileLocation asc;

-- find the all the files with person id and date 
SELECT * FROM filewithperson as fp
join files as f
on fp.fileId = f.fileId and (personId=29 or personId=30 or personId=28 or personId=31 or null)
group by fp.fileId
having (((DATE(STR_TO_DATE(f.date,'%Y-%m-%d')) >= DATE(STR_TO_DATE('2000-12-12','%Y-%m-%d'))) or (f.date is null)));


-- find the all the files with persons id and date between
SELECT * FROM filewithperson as fp
join files as f
on fp.fileId = f.fileId and (personId=29 or personId=30 or personId=28 or personId=31 or personId=35 or (f.date is null))
group by fp.fileId
having  ((DATE(STR_TO_DATE(f.date,'%Y-%m-%d')) between DATE(STR_TO_DATE('1985-12-30','%Y-%m-%d')) and  DATE(STR_TO_DATE('2020-12-30','%Y-%m-%d'))) or (f.date is null));

