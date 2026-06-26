package com.claude.poem.data.local

data class Painting(
    val id: Int,
    val title: String,
    val artist: String,
    val dynasty: String,
    val url: String,
)

object PaintingsCatalog {
    val all: List<Painting> = listOf(
        Painting(id = 49177, title = "Outing to Zhang Gong's Grotto", artist = "Shitao (Zhu Ruoji)", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153947.jpg"),
        Painting(id = 44759, title = "Landscapes, Figures, and Flowers", artist = "Chen Hongshou", dynasty = "Ming dynasty (1368-1644)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153705.jpg"),
        Painting(id = 65555, title = "Landscapes", artist = "Xiao Yuncong", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP164311.jpg"),
        Painting(id = 40057, title = "Fisherman's Lodge At Mount Xisai", artist = "Li Jie", dynasty = "Southern Song dynasty (1127-1279)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP-15820-005_crd.jpg"),
        Painting(id = 72710, title = "Landscape", artist = "Monk Jie", dynasty = "Southern Song dynasty (1127-1279)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP-17125-001.jpg"),
        Painting(id = 36131, title = "Landscapes with poems", artist = "Gong Xian", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/1981_4_1a.jpg"),
        Painting(id = 45650, title = "Landscape", artist = "Zhao Yuan", dynasty = "Yuan-Ming dynasty", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153809.jpg"),
        Painting(id = 49454, title = "Landscape", artist = "Hu Yuan", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153975.jpg"),
        Painting(id = 49134, title = "Landscape", artist = "Liu Yu", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153939.jpg"),
        Painting(id = 49055, title = "Landscape", artist = "Chen Hongshou", dynasty = "Ming dynasty (1368-1644)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153873.jpg"),
        Painting(id = 49455, title = "Landscape", artist = "Hu Yuan", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153976.jpg"),
        Painting(id = 51375, title = "Landscape", artist = "Song Xu", dynasty = "Ming dynasty (1368-1644)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP154224.jpg"),
        Painting(id = 36017, title = "Landscape", artist = "Tang Yifen", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP-14140-001.jpg"),
        Painting(id = 36191, title = "Landscape", artist = "Zhang Shanghe", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153925.jpg"),
        Painting(id = 36102, title = "Landscape", artist = "Fang Shishu", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153920.jpg"),
        Painting(id = 52225, title = "Landscape", artist = "Yun Shouping", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP154089.jpg"),
        Painting(id = 48968, title = "Landscape", artist = "Zhang Ruitu", dynasty = "Ming dynasty (1368-1644)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153868.jpg"),
        Painting(id = 44581, title = "Landscape", artist = "Feng Qiyong", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153703.jpg"),
        Painting(id = 51822, title = "Landscape", artist = "Zhang Cining", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP154246.jpg"),
        Painting(id = 44626, title = "Landscape", artist = "Wang Duo", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153702.jpg"),
        Painting(id = 49458, title = "Landscape", artist = "Zhang Xiong", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153977.jpg"),
        Painting(id = 36155, title = "Landscape", artist = "Cheng Tinglu", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153921.jpg"),
        Painting(id = 36019, title = "Landscape", artist = "Wen Boren", dynasty = "Ming-Qing dynasty", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP-14140-002.jpg"),
        Painting(id = 49013, title = "Landscape", artist = "Zou Zhilin", dynasty = "Ming-Qing dynasty", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153864.jpg"),
        Painting(id = 49175, title = "Landscape", artist = "Shitao (Zhu Ruoji)", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153946.jpg"),
        Painting(id = 36181, title = "Landscape", artist = "Wu Guxiang", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153924.jpg"),
        Painting(id = 36154, title = "Landscape", artist = "Wang Tingru", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153922.jpg"),
        Painting(id = 49253, title = "Landscape", artist = "Wang Chen", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153958.jpg"),
        Painting(id = 36186, title = "Landscape", artist = "Song Nian", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153923.jpg"),
        Painting(id = 51481, title = "Landscape", artist = "Unidentified artist", dynasty = "Ming-Qing dynasty", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP154226.jpg"),
        Painting(id = 51498, title = "Landscape", artist = "Unidentified artist", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP154241.jpg"),
        Painting(id = 51378, title = "Landscape", artist = "Unidentified artist", dynasty = "Ming-Qing dynasty", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP154225.jpg"),
        Painting(id = 35979, title = "Landscape", artist = "Unidentified artist", dynasty = "Ming dynasty (1368-1644) or later", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP-14140-005.jpg"),
        Painting(id = 51369, title = "Landscape", artist = "Unidentified artist", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP154223.jpg"),
        Painting(id = 48872, title = "Landscape", artist = "Unidentified artist", dynasty = "Ming dynasty (1368-1644)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153866.jpg"),
        Painting(id = 35974, title = "Landscape", artist = "Unidentified artist", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP-14140-006.jpg"),
        Painting(id = 51500, title = "Landscape", artist = "Unidentified artist", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP154242.jpg"),
        Painting(id = 51479, title = "Landscape", artist = "Unidentified artist", dynasty = "Ming-Qing dynasty", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP154228.jpg"),
        Painting(id = 48933, title = "Landscape", artist = "Unidentified artist", dynasty = "China", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153862.jpg"),
        Painting(id = 51305, title = "Landscape", artist = "Unidentified artist", dynasty = "Ming-Qing dynasty", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP154222.jpg"),
        Painting(id = 40409, title = "Landscape", artist = "Unidentified artist", dynasty = "Ming-Qing dynasty", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP-13609-001.jpg"),
        Painting(id = 51808, title = "Landscape", artist = "Unidentified artist", dynasty = "Ming-Qing dynasty", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP154244.jpg"),
        Painting(id = 51645, title = "Landscape", artist = "Unidentified artist", dynasty = "Ming-Qing dynasty", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP154230.jpg"),
        Painting(id = 51806, title = "Landscape", artist = "Unidentified artist", dynasty = "Ming dynasty (1368-1644)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP154243.jpg"),
        Painting(id = 51701, title = "Landscape", artist = "Unidentified artist", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP154234.jpg"),
        Painting(id = 51502, title = "Landscape", artist = "Unidentified artist", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP154245.jpg"),
        Painting(id = 51503, title = "Landscape", artist = "Unidentified artist", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP154238.jpg"),
        Painting(id = 36156, title = "Landscape", artist = "Shen Zhuo", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153926.jpg"),
        Painting(id = 40426, title = "Landscape", artist = "Unidentified artist", dynasty = "Ming dynasty (1368-1644)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP-13609-002.jpg"),
        Painting(id = 49125, title = "Landscape", artist = "Li Yu", dynasty = "Qing dynasty (1644-1911)", url = "https://images.metmuseum.org/CRDImages/as/web-large/DP153932.jpg"),
    )

    fun getDailyPainting(): Painting {
        val dayOfYear = java.time.LocalDate.now().dayOfYear
        return all[dayOfYear % all.size]
    }
}
