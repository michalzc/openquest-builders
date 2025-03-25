package michalz.openquest.tools.bestiary.json

import io.circe.{Decoder, Encoder, HCursor, Json}
import io.circe.Decoder.Result
import io.circe.syntax.*

import michalz.openquest.tools.bestiary.model.Weapon

object Item:

  given WeaponHandsDecoder: Decoder[Weapon.Hands] =
    Decoder[String].map(Weapon.Hands.valueOf)
  given WeaponStateDecoder: Decoder[Weapon.State] =
    Decoder[String].map(Weapon.State.valueOf)
  given WeaponTypeDecoder: Decoder[Weapon.Type] =
    Decoder[String].map(Weapon.Type.valueOf)

  given WeaponHandsEncoder: Encoder[Weapon.Hands] = Encoder.encodeString.contramap(_.toString)
  given WeaponStateEncoder: Encoder[Weapon.State] = Encoder.encodeString.contramap(_.toString)
  given WeaponTypeEncoder: Encoder[Weapon.Type]   = Encoder.encodeString.contramap(_.toString)

  given WeaponEncoder: Encoder[Weapon] = new Encoder[Weapon]:
    override def apply(weapon: Weapon): Json = Json.obj(
      "name" -> weapon.name.asJson,
      "type" -> "weapon".asJson,
      "img"  -> weapon.img.asJson,
      "system" -> Json.obj(
        "correspondingSkill" -> Json.obj(
          "skillReference" -> weapon.skillReference.asJson
        ),
        "hands" -> weapon.hands.asJson,
        "state" -> weapon.state.asJson,
        "type"  -> weapon.`type`.asJson,
        "damage" -> Json.obj(
          "damageFormula"    -> weapon.damageFormula.asJson,
          "includeDamageMod" -> weapon.includeDamageMod.asJson
        )
      ),
      "ownership" -> Json.obj(
        "default" -> 0.asJson
      ),
      "_key" -> weapon.key.asJson
    )

  given WeaponDecoder: Decoder[Weapon] = new Decoder[Weapon]:
    override def apply(c: HCursor): Result[Weapon] = for {
      id   <- c.downField("_id").as[Option[String]]
      name <- c.downField("name").as[String]
      img  <- c.downField("img").as[String]
      system = c.downField("system")
      skillRef   <- system.downField("correspondingSkill").downField("skillReference").as[String]
      hands      <- system.downField("hands").as[Weapon.Hands]
      state      <- system.downField("state").as[Weapon.State]
      weaponType <- system.downField("type").as[Weapon.Type]
      damage = system.downField("damage")
      damageFormula <- damage.downField("damageFormula").as[String]
      includeDM     <- damage.downField("includeDamageMod").as[Boolean]
      key           <- c.downField("_key").as[Option[String]]
    } yield Weapon(
      id = id,
      name = name,
      img = img,
      skillReference = skillRef,
      hands = hands,
      state = state,
      `type` = weaponType,
      damageFormula = damageFormula,
      includeDamageMod = includeDM,
      key = key
    )
