package com.omsk.railwaymuseum.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.omsk.railwaymuseum.R

class GameRulesFragment : Fragment() {

    private val args: GameRulesFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_game_rules, container, false)

        val textView = root.findViewById<TextView>(R.id.game_rules)
        textView.text = args.gameId.toString()

        return root
    }

}