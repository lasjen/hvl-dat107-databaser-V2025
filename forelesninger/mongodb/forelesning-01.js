show dbs
use forelesning-01

db.demo.deleteMany({});
db.demo.insertMany([
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
        price: {"$numberdesimal":"389.90"},
        published: {"$date": "2009-01-02"},
        created: {"$date": "2025-03-02"}
    }
]);

db.demo.find();
db.demo.find({price2: {$type: "number"}});