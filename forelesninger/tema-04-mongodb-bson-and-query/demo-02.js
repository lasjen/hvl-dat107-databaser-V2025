show dbs
use forelesning-02

// Clean
db.books.drop();

// CRUD - Create some data
db.books.insertMany([
    {
        title: "Min kamp",
        author: "Karl Ove Knausgård",
        year: 2024,
        pages: Int32(303),
        price: Decimal128("359.90"),
        published: ISODate("2009-04-03"),
        created: new Date()
    },
    {
        title: "Java – For dummies",
        author: "Barry A. Burd",
        year: 2024,
        pages: 432,
        price: NumberDecimal("389.90"),
        published: ISODate("2009-01-02"),
        created: new Date()
    },
    {
        title: "Python – An introduction",
        author: "P. A. Nobody",
        year: 2019,
        pages: 101,
        price: NumberDecimal("109.90"),
        published: ISODate("2019-10-01"),
        created: new Date()
    },
    {
        title: "Harry Potter",
        author: "J K. Rowling",
        year: 2005,
        pages: 409,
        price: NumberDecimal("329.99"),
        published: ISODate("2005-07-21"),
        created: new Date()
    },
    {
        title: "Everyday Italian Cooking",
        author: "Giada De Laurentiis",
        year: 2005,
        pages: 300,
        price: NumberDecimal("330.00"),
        published: ISODate("2017-03-03"),
        created: new Date()
    },
    {
        title: "XQuery Kick Start",
        author: ["James McGovern", "Per Bothner", "Kurt Cagle", "James Linn", "Vaidyanathan Nagarajan"],
        year: 2003,
        pages: 400,
        price: NumberDecimal("449.99"),
        published: ISODate("2003-01-01"),
        created: new Date()
    },
    {
        title: "Learning XML",
        author: "Erik T. Ray",
        year: 2003,
        pages: 400,
        price: NumberDecimal("39.95"),
        published: ISODate("2011-11-01"),
        created: new Date()
    }
]);

// CRUD - Read (continues)
db.books.find()
db.books.find({title: "Min kamp"})
db.books.find({title: "Min kamp", author: "Karl Ove Knausgård"})
db.books.find({title: "Min kamp", author: "Karl Ove Knausgård", year: 2024})

db.books.find({ title: "min kamp" });
db.books.find({ title: /min kamp/i });
db.books.find({ title: /in/i });
db.books.find({ title: { $regex: /in/i } });

db.books.find({ author: /^p/i });           // starter på
db.books.find({ author: /gård$/i });        // slutter på
db.books.find({ author: /^j.*n$/i });       // start og slutter på
db.books.find({ author: { $not: /^j/i }});
db.books.find({ author: /^[a-å]r/i });

// AND
db.books.find({ author: /^Erik/, year: 2003 });

// Projeksjon
db.books.find({ year: 2003 }, { author:1 });
db.books.find({ year: 2003 }, { _id: 0, author:1 });

// Hent authors fra alle bøker
db.books.find({}, { author: 1 })

// Size, Count og Limit
db.books.find({ author: /^P/i }).count();
db.books.find({ author: /^P/i }).size();
db.books.find({ author: /^P/i })
db.books.find({ author: /^P/i }).limit(1)

// Sortering
db.books.find({}).sort({ title: 1 });

// Eiere
db.eiere.drop();
db.eiere.insertMany([
    { navn: "Kari", by: "Tromsø", alder: 25 },
    { navn: "Per", by: "Oslo", alder: 35 },
    { navn: "Ola", by: "Bergen", alder: 40 },
    { navn: "Anne", by: "Stavanger", alder: 30 },
    { navn: "Karianne", by: "Bergen>", alder: 28 },
    { navn: "Per Olav", by: "Stavanger", alder: 45 },
    { navn: "Kari Anne", by: "Oslo", alder: 50 },
    { navn: "Johanne", by: "Levanger", alder: 55 },
    { navn: "Line", by: "Oslo", alder: 60 },
    { navn: "Stine", by: "Stavanger", alder: 65 },
    { navn: "Katrine", by: "Tromsø", alder: 70 },
    { navn: "Marius", by: "Oslo", alder: 75 },
    { navn: "Henrik", by: "Stavanger", alder: 80 },
    { navn: "Lars", by: "Haugesund", alder: 85 },
    { navn: "Martin", by: "Molde", alder: 90 },
    { navn: "Sofie", by: "Alta", alder: 95 },
    { navn: "Emilie", by: "Verdal", alder: 100 },
    { navn: "Andreas", by: "Dombås", alder: 9 },
    { navn: "Kristoffer", by: "Hammerfest", alder: 10 },
    { navn: "Elias", by: "Kirkenes", alder: 15 },
    { navn: "Benjamin", by: "Bodø", alder: 20 },
    { navn: "Sander", by: "Narvik", alder: 25 },
    { navn: "Oskar", by: "Tromsø", alder: 30 },
    { navn: "Filip", by: "Stavanger", alder: 35 },
    { navn: "Jakob", by: "Oslo", alder: 40 },
    { navn: "Mikkel", by: "Bergen", alder: 45 },
    { navn: "Noah", by: "Drammen", alder: 50 },
    { navn: "Oliver", by: "Fredrikstad", alder: 55 },
    { navn: "Liam", by: "Kristiansand", alder: 60 },
    { navn: "Lucas", by: "Bodø", alder: 65 },
    { navn: "Aksel", by: "Haugesund", alder: 70 },
    { navn: "Marius", by: "Stavanger", alder: 75 },
    { navn: "Magnus", by: "Oslo", alder: 80 },
    { navn: "Jonas", by: "Tromsø", alder: 85 },
    { navn: "Filip", by: "Bergen", alder: 7 },
    { navn: "Herman", by: "Stavanger", alder: 37 },
    { navn: "Sondre", by: "Oslo", alder: 33 },
    { navn: "Sindre", by: "Tromsø", alder: 29 },
    { navn: "Simen", by: "Bergen", alder: 26 },
    { navn: "Stian", by: "Stavanger", alder: 23 },
    { navn: "Stefan", by: "Oslo", alder: 20 },
    { navn: "Sigurd", by: "Tromsø", alder: 19 },
    { navn: "Sebastian", by: "Bergen", alder: 18 },
    { navn: "Sverre", by: "Stavanger", alder: 17 },
    { navn: "Sigve", by: "Oslo", alder: 16 },
    { navn: "Sindre", by: "Tromsø", alder: 15 },
    { navn: "Sondre", by: "Bergen", alder: 14 },
    { navn: "Simen", by: "Stavanger", alder: 13 },
    { navn: "Jonny", by: "Oslo", alder: 12 },
    { navn: "Stefan", by: "Tromsø", alder: 11 },
    { navn: "Sigurd", by: "Bergen", alder: 10 },
    { navn: "Roald", by: "Os", alder: 2 },
    { navn: "Ole", by: "Gokk", alder: 78 },
    { navn: "Ole", by: "Sørlia", alder: 33 },
    { navn: "Ole", by: "Nord", alder: 14 },
    { navn: "Ole", by: "Sør", alder: 78 }
]);

db.eiere.find().count()

db.eiere.find({ by: "Oslo" },{_id: 0, navn:1}).sort({navn: -1}).limit(3)
db.eiere.find({ by: "Oslo" },{_id: 0, navn:1}).limit(3).sort({navn: -1})

// eq
db.eiere.find({ alder: { $eq: 25 }})

db.eiere.find({ alder: 25 })

// ne
db.eiere.find({by:{$ne:"Oslo"}})

// in + nin
db.eiere.find({ by: { $in: ["Stavanger", "Levanger"] } })
db.eiere.find({ by: { $nin: ["Stavanger", "Levanger"] } })

// gt + gte, lt + lte
db.eiere.find({ alder: { $lt: 7 } })
db.eiere.find({ alder: { $lte: 7 } })

db.eiere.find({ alder: { $gt: 95 } })
db.eiere.find({ alder: { $gte: 95 } })

// and or
db.eiere.find({$and:[{by:{$eq:"Oslo"}},{alder: {$lt:15}}]})
db.eiere.find({$or:[{by:{$eq:"Oslo"}},{alder: {$lt:15}}]})

// not
db.eiere.find({by:{$not:{$eq:"Oslo"}}})
db.eiere.find({by:{$ne:"Oslo"}})

// nor
db. eiere.find( { $nor: [ {by:"Oslo"}, { alder: {$gte: 10} } ]})

// expr
db.monthlyBudget.drop()
db.monthlyBudget.insertMany( [
    { _id : 1, category : "food",    budget : 400, spent : 450 },
    { _id : 2, category : "drinks",  budget : 100, spent : 150 },
    { _id : 3, category : "clothes", budget : 100, spent :  50 },
    { _id : 4, category : "misc",    budget : 500, spent : 300 },
    { _id : 5, category : "travel",  budget : 200, spent : 650 }
]);

// Find categories where spent more than budget
db.monthlyBudget.find({ $expr: { $gt: [ "$spent", "$budget" ] } });


// What if x=0
db.dummy.drop();
db.dummy.insertMany([
    {x: 0, y: 0},{x: 1, y: 0},{x: 2, y: 0},{x: 3, y: 0},
    {x: 0, y: 1},{x: 1, y: 1},{x: 2, y: 1},{x: 3, y: 1},
    {x: 0, y: 2},{x: 1, y: 2},{x: 2, y: 2},{x: 3, y: 2},
    {x: 0, y: 3},{x: 1, y: 3},{x: 2, y: 3},{x: 3, y: 3}
]);


db.dummy.find({$expr: {$eq:[ {$divide:["$y","$x"]},1]}})

db.dummy.find( {
    $and: [
        { x: { $ne: 0 } },
        {$expr: {$eq:[ {$divide:["$y","$x"]},1]}}
    ]
})

// CRUD - Update
var objId = db.eiere.find({ navn: "Kari" }, { _id: 1 }).toArray()[0]._id;
db.eiere.updateOne(
    { _id: objId},
    { $set:  { by: "Oslo" } }
);

// Kunne også ha funnet id-en med, og kopiert
db.eiere.find({ navn: "Kari" })
db.eiere.updateOne(
    { _id: ObjectId("67ffcbc47bfeb55bdd93c0a2")},
    { $set:  { by: "Tromsø" } }
);

// CRUD - replace (en form for update)
db.eiere.find({ navn: "Kari" })
db.eiere.replaceOne(
    { _id: ObjectId('67ffcbc47bfeb55bdd93c0a2')},
    { navn: "Lise", by: "Bergen" }
);
db.eiere.find({ navn: "Lise" })

// CRUD - upsert (fremdeles en type update)
db.eiere.find({ navn: "Karl" })
db.eiere.updateOne( // Karl eksisterer ikke
    { navn: "Karl" },
    { $setOnInsert: { alder: 40, by: "Oslo" } },
    { upsert: true }
);

db.eiere.updateOne( // Nå eksisterer Karl
    { navn: "Karl" },
    { $setOnInsert: { alder: 50 } },
    { upsert: true }
);

db.eiere.updateOne( // Karl eksiterer, benytter set istedet for setOnInsert
    { navn: "Karl" },
    { $set: { alder: 51 } },
    { upsert: true }
);

db.eiere.find({ navn: "Carl" })
db.eiere.updateOne( // Carl eksisterer ikke, upsert false
    { navn: "Carl" },
    { $set: { alder: 26 } },
    { upsert: false }
);

db.eiere.updateOne( // Carl eksisterer ikke, upsert false (default)
    { navn: "Carl" },
    { $set: { alder: 26 } }
);

db.eiere.updateOne( // Carl eksisterer ikke, upsert false (default)
    { navn: "Carl" },
    { $setOnInsert: { alder: 26 } }
);

// Find and Update (with returnDocument)
db.eiere.find({ navn: "Kari" })
db.eiere.findOneAndUpdate(  // Kari eksisterer
    { navn: "Kari" },
    { $set: { alder: 26 } }
);
db.eiere.findOneAndUpdate(  // Kari eksisterer
    { navn: "Kari" },
    { $set: { alder: 27 } },
    { returnDocument: "after" }
);

// CRUD - Delete
db.eiere.find({ navn: "Kari" })
db.eiere.findOneAndDelete({ navn: "Kari" })
db.eiere.find({ navn: "Kari" })

db.eiere.find({navn: "Ole"}).count();
db.eiere.deleteOne({ navn: "Ole"})
db.eiere.deleteMany({ navn: "Ole"})
