/*
 * MongoDB - Tema 4: BSON og spørringer
 *
 * Forelesning 3: Datamodellering
 *
 * INFO! Denne forelesningen er flyttet til siste del av NOSQL-delen. Ignorer før dette.
 *       I disse eksemplene er det brukt funksjonalitet som dere IKKE trenger å kunne til eksamen.
 *       Eksemplene er kun ment som en demonstrasjon av hva som er mulig å gjøre med MongoDB.
 */
show dbs
use forelesning-03

db.ansatte.drop()
db.ansattehistorie.drop()

//
var objId = new ObjectId()
var oppdDato = new ISODate()
db.ansatte.insertOne(
    {
            _id: objId,
            revisjon: 1,
            oppdatert: oppdDato,
            fornavn: "Ola",
            etternavn: "Hansen",
            kontor: {rom: 401, etasje: 4, fløy: "B"}
    }
)

db.ansattehistorie.insertOne(
    {
            _id: objId,
            revisjon: 1,
            oppdatert: oppdDato,
            fornavn: "Ola",
            etternavn: "Hansen",
            kontor: {rom: 401, etasje: 4, fløy: "A"}
    }
)

db.ansatte.find()
db.ansattehistorie.find()

// Så endrer vi rom for Ola
db.ansatte.updateOne(
    { _id: objId },
    { $set: { kontor: {rom: 220, etasje: 2, fløy: "B"} },
            $inc: { revisjon: 1 },
            $currentDate: { oppdatert: true }
    }
)

db.ansatte.find()
db.ansattehistorie.find()

// Oppdaterer historien
db.ansatte.aggregate( [
        { $match: {_id: objId } },
        { $set: { _id: new ObjectId() } },
        { $merge: {
                        into: { db: "forelesning-03",
                                coll: "ansattehistorie" },
                        on: "_id",
                        whenNotMatched: "insert"}
        }
] )
