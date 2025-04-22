show dbs
use forelesning-01

// Clean
db.demo.deleteMany({});

// BSON Datatyper - Int32 - Decimal128 - ISODate - ObjectId (Hva skjer hvis vi bruker "new Date()"?)
db.demo.insertOne(
    {
        title: "Min kamp",
        author: "Karl Ove Knausgård",
        year: 2024,
        pages: Int32(303),
        price: Decimal128("359.90"),
        published: ISODate("2009-04-03"),
        created: new Date(),
        created2: Date()
    });

// Datatypes defines with "$date" and "$numberDecimal"
// Recommended to use BSON types instead. These format is better for interoperability
db.demo.insertOne({
    title: "Java – For dummies",
    author: "Barry A. Burd",
    year: 2024,
    pages: 432,
    price: {"$numberDecimal":"389.90"},
    published: {"$date": "2009-01-02"},
    created: {"$date": "2025-03-02"}
});

db.demo.find();
db.demo.find({price: {$type: "number"}});