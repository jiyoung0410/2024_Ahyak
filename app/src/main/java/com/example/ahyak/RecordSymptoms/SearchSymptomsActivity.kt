package com.example.ahyak.RecordSymptoms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ahyak.DB.AhyakDataBase
import com.example.ahyak.databinding.ActivitySearchSymptomsBinding

class SearchSymptomsActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var binding : ActivitySearchSymptomsBinding

    private var searchSymptoms : ArrayList<DataItemSearchSymptom> = arrayListOf()
    private var searchSymptomsadapter : SearchSymptomsAdapter? = null
    //DataBase 객체
    var ahyakDatabase : AhyakDataBase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchSymptomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ahyakDatabase = AhyakDataBase.getInstance(this)
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("가래"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("가려움"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("가스참"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("가슴 답답함"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("가슴 두근거림"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("가슴 통증"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("감각이상"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("감정기복"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("고혈당"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("과다 수면"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("과호흡"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("관절 부종"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("관절통"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("괴사"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("구내 마비감"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("구내염"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("구순염"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("구역감"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("구토"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("귀 통증"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("균형이상"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("근무력증"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("근육경련"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("근육경직"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("근육통"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("기관지경련"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("기관지수축"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("기억 장애"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("기침"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("나른함"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("난청"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("눈 가려움"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("눈 건조감"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("눈 부종"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("눈 충혈"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("눈 피로감"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("눈물흘림"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("눈부심"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("느린 맥"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("다리 부종"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("동공확대"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("두통"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("등 통증"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("딸국질"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("떨림"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("마비"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("머리 무거움"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("목마름"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("무력감"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("물집"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("묽은 변"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("미각 이상"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("발기부전"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("발열"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("발진"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("발한"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("배뇨곤란"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("변비"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("복부팽만"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("복통"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("부종"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("불규칙한 심박"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("불면"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("불안"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("붉은색 반점"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("빠른 맥"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("설사"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("성욕감퇴"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("소변 감소"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("소화불량"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("속쓰림"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("수면 장애"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("수족 냉증"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("수족 마비"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("수족 부종"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("수족 저림"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("시각 장애"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("식욕부진"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("식욕증가"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("신체 발모"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("안면홍조"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("어지러움"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("언어 장애"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("얼굴 부종"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("얼굴 열감"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("여드름"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("연하 곤란"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("오한"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("우울감"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("월경외출혈"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("월경장애"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("월경통"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("위 통증"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("위경련"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("유방 통증"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("의식 장애"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("이명"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("인후통"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("입마름"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("잇몸 염증"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("잇몸 출혈"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("자주색 반점"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("잔뇨감"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("재채기"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("저림"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("저혈당"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("전신부종"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("전신홍조"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("정신 흥분"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("졸음"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("지혈 지연"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("질 분비물 이상"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("집중 장애"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("창백한 피부"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("청색증"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("체온강하"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("체중감소"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("체중증가"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("체취증가"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("치통"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("코막힘"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("코피"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("콧물"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("탈모"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("판단력 장애"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("피로"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("피부 건조"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("피부염"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("허리 통증"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("혀 감각이상"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("혈뇨"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("혈담"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("혈변"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("혈압 강하"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("혈압 상승"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("혈토"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("호흡곤란"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("화끈거림"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("환각"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("황달"))
//        ahyakDatabase!!.getAhyakDao().add(AhyakEntity("흑색 변"))
//
//        searchSymptoms.addAll(ahyakDatabase!!.getAhyakDao().getAllMyStrings())


        searchSympromsInit()
        initsearchSymptomsadapter()

        //'x'누르면
        binding.recordSymptomsCancleIv.setOnClickListener {
            finish()
        }

        //Edit Text 관련
        binding.searchSymptomsEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력 전에 실행할 작업
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트가 변경될 때마다 실행할 작업
                filterSymptoms(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // 입력 후에 실행할 작업
            }
        })

    }

    private fun initsearchSymptomsadapter() {
        searchSymptomsadapter = SearchSymptomsAdapter(searchSymptoms, this)
        binding.searchSymptomsRv.adapter = searchSymptomsadapter
        binding.searchSymptomsRv.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false )
    }

    private fun filterSymptoms(query: String) {
        val filteredList = ArrayList<DataItemSearchSymptom>()

        for (item in searchSymptoms) {
            // 증상 명칭에 검색어가 포함되어 있는지 확인
            if (item.searchsympotmName.contains(query, ignoreCase = true)) {
                filteredList.add(item)
            }
        }

        // 어댑터에 필터링된 목록 설정
        searchSymptomsadapter?.filterList(filteredList)
    }

    private fun searchSympromsInit() {
        searchSymptoms.addAll(
            arrayListOf(
                DataItemSearchSymptom("가래"),
                DataItemSearchSymptom("가려움"),
                DataItemSearchSymptom("가스참"),
                DataItemSearchSymptom("가슴 답답함"),
                DataItemSearchSymptom("가슴 두근거림"),
                DataItemSearchSymptom("가슴 통증"),
                DataItemSearchSymptom("감각이상"),
                DataItemSearchSymptom("감정기복"),
                DataItemSearchSymptom("고혈당"),
                DataItemSearchSymptom("과다 수면"),
                DataItemSearchSymptom("과호흡"),
                DataItemSearchSymptom("관절 부종"),
                DataItemSearchSymptom("관절통"),
                DataItemSearchSymptom("괴사"),
                DataItemSearchSymptom("구내 마비감"),
                DataItemSearchSymptom("구내염"),
                DataItemSearchSymptom("구순염"),
                DataItemSearchSymptom("구역감"),
                DataItemSearchSymptom("구토"),
                DataItemSearchSymptom("귀 통증"),
                DataItemSearchSymptom("균형이상"),
                DataItemSearchSymptom("근무력증"),
                DataItemSearchSymptom("근육경련"),
                DataItemSearchSymptom("근육경직"),
                DataItemSearchSymptom("근육통"),
                DataItemSearchSymptom("기관지경련"),
                DataItemSearchSymptom("기관지수축"),
                DataItemSearchSymptom("기억 장애"),
                DataItemSearchSymptom("기침"),
                DataItemSearchSymptom("나른함"),
                DataItemSearchSymptom("난청"),
                DataItemSearchSymptom("눈 가려움"),
                DataItemSearchSymptom("눈 건조감"),
                DataItemSearchSymptom("눈 부종"),
                DataItemSearchSymptom("눈 충혈"),
                DataItemSearchSymptom("눈 피로감"),
                DataItemSearchSymptom("눈물흘림"),
                DataItemSearchSymptom("눈부심"),
                DataItemSearchSymptom("느린 맥"),
                DataItemSearchSymptom("다리 부종"),
                DataItemSearchSymptom("동공확대"),
                DataItemSearchSymptom("두통"),
                DataItemSearchSymptom("등 통증"),
                DataItemSearchSymptom("딸국질"),
                DataItemSearchSymptom("떨림"),
                DataItemSearchSymptom("마비"),
                DataItemSearchSymptom("머리 무거움"),
                DataItemSearchSymptom("목마름"),
                DataItemSearchSymptom("무력감"),
                DataItemSearchSymptom("물집"),
                DataItemSearchSymptom("묽은 변"),
                DataItemSearchSymptom("미각 이상"),
                DataItemSearchSymptom("발기부전"),
                DataItemSearchSymptom("발열"),
                DataItemSearchSymptom("발진"),
                DataItemSearchSymptom("발한"),
                DataItemSearchSymptom("배뇨곤란"),
                DataItemSearchSymptom("변비"),
                DataItemSearchSymptom("복부팽만"),
                DataItemSearchSymptom("복통"),
                DataItemSearchSymptom("부종"),
                DataItemSearchSymptom("불규칙한 심박"),
                DataItemSearchSymptom("불면"),
                DataItemSearchSymptom("불안"),
                DataItemSearchSymptom("붉은색 반점"),
                DataItemSearchSymptom("빠른 맥"),
                DataItemSearchSymptom("설사"),
                DataItemSearchSymptom("성욕감퇴"),
                DataItemSearchSymptom("소변 감소"),
                DataItemSearchSymptom("소화불량"),
                DataItemSearchSymptom("속쓰림"),
                DataItemSearchSymptom("수면 장애"),
                DataItemSearchSymptom("수족 냉증"),
                DataItemSearchSymptom("수족 마비"),
                DataItemSearchSymptom("수족 부종"),
                DataItemSearchSymptom("수족 저림"),
                DataItemSearchSymptom("시각 장애"),
                DataItemSearchSymptom("식욕부진"),
                DataItemSearchSymptom("식욕증가"),
                DataItemSearchSymptom("신체 발모"),
                DataItemSearchSymptom("안면홍조"),
                DataItemSearchSymptom("어지러움"),
                DataItemSearchSymptom("언어 장애"),
                DataItemSearchSymptom("얼굴 부종"),
                DataItemSearchSymptom("얼굴 열감"),
                DataItemSearchSymptom("여드름"),
                DataItemSearchSymptom("연하 곤란"),
                DataItemSearchSymptom("오한"),
                DataItemSearchSymptom("우울감"),
                DataItemSearchSymptom("월경외출혈"),
                DataItemSearchSymptom("월경장애"),
                DataItemSearchSymptom("월경통"),
                DataItemSearchSymptom("위 통증"),
                DataItemSearchSymptom("위경련"),
                DataItemSearchSymptom("유방 통증"),
                DataItemSearchSymptom("의식 장애"),
                DataItemSearchSymptom("이명"),
                DataItemSearchSymptom("인후통"),
                DataItemSearchSymptom("입마름"),
                DataItemSearchSymptom("잇몸 염증"),
                DataItemSearchSymptom("잇몸 출혈"),
                DataItemSearchSymptom("자주색 반점"),
                DataItemSearchSymptom("잔뇨감"),
                DataItemSearchSymptom("재채기"),
                DataItemSearchSymptom("저림"),
                DataItemSearchSymptom("저혈당"),
                DataItemSearchSymptom("전신부종"),
                DataItemSearchSymptom("전신홍조"),
                DataItemSearchSymptom("정신 흥분"),
                DataItemSearchSymptom("졸음"),
                DataItemSearchSymptom("지혈 지연"),
                DataItemSearchSymptom("질 분비물 이상"),
                DataItemSearchSymptom("집중 장애"),
                DataItemSearchSymptom("창백한 피부"),
                DataItemSearchSymptom("청색증"),
                DataItemSearchSymptom("체온강하"),
                DataItemSearchSymptom("체중감소"),
                DataItemSearchSymptom("체중증가"),
                DataItemSearchSymptom("체취증가"),
                DataItemSearchSymptom("치통"),
                DataItemSearchSymptom("코막힘"),
                DataItemSearchSymptom("코피"),
                DataItemSearchSymptom("콧물"),
                DataItemSearchSymptom("탈모"),
                DataItemSearchSymptom("판단력 장애"),
                DataItemSearchSymptom("피로"),
                DataItemSearchSymptom("피부 건조"),
                DataItemSearchSymptom("피부염"),
                DataItemSearchSymptom("허리 통증"),
                DataItemSearchSymptom("혀 감각이상"),
                DataItemSearchSymptom("혈뇨"),
                DataItemSearchSymptom("혈담"),
                DataItemSearchSymptom("혈변"),
                DataItemSearchSymptom("혈압 강하"),
                DataItemSearchSymptom("혈압 상승"),
                DataItemSearchSymptom("혈토"),
                DataItemSearchSymptom("호흡곤란"),
                DataItemSearchSymptom("화끈거림"),
                DataItemSearchSymptom("환각"),
                DataItemSearchSymptom("황달"),
                DataItemSearchSymptom("흑색 변")

            )
        )
    }

    override fun onItemClick(searchSymptom: DataItemSearchSymptom) {
        val intent = Intent(this, DegreeSymptomsActivity::class.java)
        intent.putExtra("searchText", searchSymptom.searchsympotmName)
        startActivity(intent)
    }
}