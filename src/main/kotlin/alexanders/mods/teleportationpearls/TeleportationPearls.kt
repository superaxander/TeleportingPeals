package alexanders.mods.teleportationpearls

import de.ellpeck.rockbottom.api.IApiHandler
import de.ellpeck.rockbottom.api.IGameInstance
import de.ellpeck.rockbottom.api.RockBottomAPI
import de.ellpeck.rockbottom.api.assets.IAssetManager
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer
import de.ellpeck.rockbottom.api.event.EventResult
import de.ellpeck.rockbottom.api.event.IEventHandler
import de.ellpeck.rockbottom.api.event.IEventListener
import de.ellpeck.rockbottom.api.event.impl.EntityTickEvent
import de.ellpeck.rockbottom.api.mod.IMod
import de.ellpeck.rockbottom.api.util.reg.IResourceName

var resource: IResourceName? = null

class TeleportationPearls : IMod {
    override fun getVersion(): String = "0.1"

    override fun getId(): String = "teleportationpearls"

    override fun getDisplayName(): String = "Teleportation Pearls"

    override fun getResourceLocation(): String = "/assets/$id"

    override fun getDescription(): String = "A simple mod that adds pearls that teleport you. What else did you expect?"

    override fun init(game: IGameInstance, assetManager: IAssetManager, apiHandler: IApiHandler, eventHandler: IEventHandler) {
        resource = RockBottomAPI.createRes(this, "pearl")
        val pearl = TeleportationPearl(resource!!)
        RockBottomAPI.ITEM_REGISTRY.register(resource, pearl)
        RockBottomAPI.ENTITY_REGISTRY.register(resource, PearlEntity::class.java)
        RockBottomAPI.getEventHandler().registerListener(EntityTickEvent::class.java, object : IEventListener<EntityTickEvent> {
            var cooldown = 0
            override fun listen(p0: EventResult, p1: EntityTickEvent): EventResult {
                if (p1.entity is AbstractEntityPlayer && cooldown-- <= 0) {
                    val player = p1.entity as AbstractEntityPlayer
                    if (player.inv[player.selectedSlot] != null && player.inv[player.selectedSlot].item is TeleportationPearl) {
                        if (RockBottomAPI.getGame().container.input.isMouseButtonDown(0)) {
                            player.inv[player.selectedSlot].removeAmount(1)
                            if (player.inv[player.selectedSlot].amount <= 0)
                                player.inv[player.selectedSlot] = null
                            val entity = PearlEntity(player, p1.entity.world)
                            entity.setPos(player.x, player.y)
                            entity.motionX += .25f * player.facing.x
                            entity.motionY += 0.5f
                            p1.entity.world.addEntity(entity)
                            cooldown = 80
                        }
                    }
                }
                return p0
            }
        })
    }


}
