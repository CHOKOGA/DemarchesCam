package com.example

import com.example.data.local.PreseedData
import com.example.ui.components.formatFcfa
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testCurrencyFormatting() {
        assertEquals("Gratuit", formatFcfa(0))
        assertEquals("10\u00A0000 FCFA", formatFcfa(10000).replace("\u202F", "\u00A0"))
        assertEquals("110\u00A0000 FCFA", formatFcfa(110000).replace("\u202F", "\u00A0"))
    }

    @Test
    fun testPreseedDataCompleteness() {
        val procedures = PreseedData.procedures
        assertTrue("Devrait contenir au moins 8 démarches phares", procedures.size >= 8)

        val cni = procedures.find { it.id == "cni" }
        assertNotNull("La démarche CNI doit être présente", cni)
        assertEquals(10000, cni?.officialCostXaf)
        assertEquals(2, cni?.officialDelayDays) // Délai officiel CNI 48h (DGSN / idcam.cm)

        val pass = procedures.find { it.id == "passeport" }
        assertNotNull("Le passeport doit être présent", pass)
        assertEquals(110000, pass?.officialCostXaf)
        assertEquals(2, pass?.officialDelayDays) // Délai officiel passeport 48h (passcam.dgsn.cm)

        val cities = PreseedData.locations.map { it.city }.toSet()
        assertTrue(cities.contains("Yaoundé"))
        assertTrue(cities.contains("Douala"))
        assertTrue(cities.contains("Bafoussam"))
    }
}
