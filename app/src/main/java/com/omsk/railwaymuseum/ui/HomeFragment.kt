package com.omsk.railwaymuseum.ui

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.glide.slider.library.SliderLayout
import com.glide.slider.library.slidertypes.DefaultSliderView
import com.omsk.railwaymuseum.R
import com.omsk.railwaymuseum.data.HomeSectionModel
import com.omsk.railwaymuseum.util.HOME_IMAGE_RATIO
import com.omsk.railwaymuseum.util.getDisplayWidth
import com.omsk.railwaymuseum.viewmodels.HomeViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val SLIDER_ANIMATION_START_DELAY = 7000L
const val SLIDER_ANIMATION_DURATION = 7000L
const val SLIDER_SHOW_DELAY = 3000L

class HomeFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Метод context.getDisplay, используемый для определения параметров экрана телефона
        //  не срабатывает на api 22, хотя на api 24 срабатывает, при этом в документации написано,
        //  что этот метод добавлен только в api 30
        val displayWidth = if (Build.VERSION.SDK_INT >= 30) {
            getDisplayWidth(view.context)
        } else {
            getDisplayWidth(requireActivity())
        }
        //Слайдеру нужно устанавливать высоту иначе он занимает весь view, соответственно
        //  заглушке слайдера устанавливается такая же высота
        val sliderHeight = (displayWidth / HOME_IMAGE_RATIO).toInt()

        //Заглушка слайдера (изображение замещающее слайдер):
        //  -   постоянно, если изображения для слайдера не загрузились;
        //  -   временно, пока изображения загружаются в слайдер.
        val sliderPlaceholder: ImageView = view.findViewById(R.id.home_slider_placeholder)
        sliderPlaceholder.layoutParams.height = sliderHeight
        Glide
            .with(this)
            .load(R.drawable.home_placeholder)
            .into(sliderPlaceholder)

        //Устанавливать 1 фото в слайдер и останавливать автолистание слайдов не выход, т.к. остается
        //  возможность листания слайдера пользователем (установка isEnabled = false эту проблему
        //  не решает), а листание одного и того же фото выглядит некрасиво.
        val sliderShow: SliderLayout = view.findViewById(R.id.home_slider)
        sliderShow.layoutParams.height = sliderHeight
        //Добавление uri картинок в слайдер с помощью live data
        val viewModel = HomeViewModel()
        viewModel.response.observe(viewLifecycleOwner, {
            it?.let {
                for (homeSliderImage in it) {
                    val sliderView = DefaultSliderView(context)
                    sliderView.image(homeSliderImage.uri)
                    sliderShow.addSlider(sliderView)
                }
                sliderShow.startAutoCycle(SLIDER_ANIMATION_START_DELAY,
                                        SLIDER_ANIMATION_DURATION,
                                        true)

                //Задержка появления слайдера, чтобы пользователь не видел процесса загрузки
                //  изображений в слайдер, т.к. это выглядит некрасиво.
                MainScope().launch {
                    delay(SLIDER_SHOW_DELAY)
                    sliderShow.visibility = VISIBLE
                }
            }
        })

        //Задание параметров секции RecyclerView
        val homeSectionRecyclerView = view.findViewById<RecyclerView>(R.id.home_recycler)
        val adapter = HomeRecyclerAdapter(HomeSectionModel.list, requireContext())
        homeSectionRecyclerView.adapter = adapter
        adapter.setListener(object : HomeRecyclerAdapter.Listener {
            override fun onClick(itemData: HomeSectionModel) {
                val actionToAfterClick = when(itemData.name) {
                    "События" -> HomeFragmentDirections.actionHomeFragmentToEventListFragment()
                    else -> null
                }
                actionToAfterClick?.let { findNavController().navigate(it) }
            }
        })
    }

}