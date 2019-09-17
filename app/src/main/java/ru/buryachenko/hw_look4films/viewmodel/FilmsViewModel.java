package ru.buryachenko.hw_look4films.viewmodel;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;

import java.util.ArrayList;

import androidx.lifecycle.ViewModel;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;

public class FilmsViewModel extends ViewModel {
    private FilmInApp[] films;
    public FilmInApp[] getList(Context context) {
        if (films == null) {
            films = new FilmInApp[]{
                    new FilmInApp("Анна", (BitmapDrawable) context.getResources().getDrawable(R.drawable.anna), "История Анны, чья несравненная красота скрывает поразительную мощь и смертоносный талант опаснейшего наемного убийцы в мире."),
                    new FilmInApp("Образование", (BitmapDrawable) context.getResources().getDrawable(R.drawable.booksmart), "Старшеклассницы Молли и Эми последние четыре года были поглощены учебой, настойчиво пытаясь заработать высокие отметки, чтобы поступить в престижные колледжи. В итоге Молли удается пробиться в Йель, а Эми получает желанное место в Колумбийском университете, но лучшие подруги не чувствуют себя счастливыми. Они осознают, что постоянное стремление быть прилежными ученицами лишило их типичных для школьников развлечений, поэтому решают как следует повеселиться в последнюю ночь перед выпускным вечером."),
                    new FilmInApp("Дитя робота", (BitmapDrawable) context.getResources().getDrawable(R.drawable.iammother), "После глобального катаклизма человечество вымирает. В подземном бункере автоматически активизируется аварийная программа, и робот-гуманоид «Мать» выращивает из эмбриона человеческого ребенка. Девушка, воспитанная под бережным присмотром «Матери», никогда не видела ни поверхности Земли, ни других людей. Но однажды ее мир переворачивается, когда на пороге убежища появляется женщина с просьбой о помощи..."),
                    new FilmInApp("Плюс один", (BitmapDrawable) context.getResources().getDrawable(R.drawable.plusone), "Лето — пора свадеб. Закоренелый холостяк Бен и его подруга Элис, которая только что рассталась со своим парнем, приглашены сразу на десять свадеб. Чтобы не искать плюс один, ребята решают притвориться влюбленной парой. Удастся ли им устоять перед магией секса вокруг и остаться друзьями?"),
                    new FilmInApp("Дылда", (BitmapDrawable) context.getResources().getDrawable(R.drawable.tallgirl), "Джоди, самая высокая девушка в школе, всегда чувствовала себя неловко в собственной шкуре. Но после многих лет насмешек и избегания внимания любой ценой, Джоди, наконец, решает обрести уверенность в себе.")
            };
        }
        return films;
    }
}
