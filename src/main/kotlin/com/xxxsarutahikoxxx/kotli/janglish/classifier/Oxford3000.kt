package com.xxxsarutahikoxxx.kotli.janglish.classifier

object Oxford3000 : Classifier {
    override fun contains(spell: String): Boolean = spell in VocabularyList

    val VocabularyList : List<String> = """
        a
        abandon
        abandoned
        ability
        able
        about
        above
        abroad
        absence
        absent
        absolute
        absolutely
        absorb
        abuse
        academic
        accent
        accept
        acceptable
        access
        accident
        accidental
        accidentally
        accommodation
        accompany
        according to
        account
        account for
        accurate
        accurately
        accuse
        achieve
        achievement
        acid
        acknowledge
        a couple
        acquire
        across
        act
        action
        active
        actively
        activity
        actor
        actress
        actual
        actually
        ad
        adapt
        add
        addition
        additional
        add on
        address
        add up
        add up to
        adequate
        adequately
        adjust
        admiration
        admire
        admit
        adopt
        adult
        advance
        advanced
        advantage
        adventure
        advert
        advertise
        advertisement
        advertising
        advice
        advise
        affair
        affect
        affection
        afford
        afraid
        after
        afternoon
        afterwards
        again
        against
        age
        aged
        agency
        agent
        aggressive
        ago
        agree
        agreement
        ahead
        aid
        aim
        air
        aircraft
        airport
        alarm
        alarmed
        alarming
        alcohol
        alcoholic
        alive
        all
        allied
        allow
        allow for
        all right
        ally
        almost
        alone
        along
        alongside
        aloud
        alphabet
        alphabetical
        alphabetically
        already
        also
        alter
        alternative
        alternatively
        although
        altogether
        always
        a.m.
        amaze
        amazed
        amazing
        ambition
        ambulance
        among
        amount
        amount to
        amuse
        amused
        amusing
        analyse
        analysis
        ancient
        and
        anger
        angle
        angrily
        angry
        animal
        ankle
        anniversary
        announce
        annoy
        annoyed
        annoying
        annual
        annually
        another
        answer
        anti-
        anticipate
        anxiety
        anxious
        anxiously
        any
        anybody
        anyone
        anything
        anyway
        anywhere
        apart
        apart from
        apartment
        apologize
        apparent
        apparently
        appeal
        appear
        appearance
        apple
        application
        apply
        appoint
        appointment
        appreciate
        approach
        appropriate
        approval
        approve
        approving
        approximate
        approximately
        April
        area
        argue
        argument
        arise
        arm
        armed
        arms
        army
        around
        arrange
        arrangement
        arrest
        arrival
        arrive
        arrive at
        arrow
        art
        article
        artificial
        artificially
        artist
        artistic
        artistically
        as
        ashamed
        aside
        aside from
        ask
        asleep
        aspect
        assist
        assistance
        assistant
        associate
        associated
        association
        assume
        assure
        at
        atmosphere
        atom
        attach
        attached
        attack
        attempt
        attempted
        attend
        attend to
        attention
        attitude
        attorney
        attract
        attraction
        attractive
        audience
        August
        aunt
        author
        authority
        automatic
        automatically
        autumn
        available
        average
        avoid
        awake
        award
        aware
        away
        awful
        awfully
        awkward
        awkwardly
        baby
        back
        background
        back up
        backward
        backwards
        bacteria
        bad
        badly
        bad-tempered
        bag
        baggage
        bake
        balance
        ball
        ban
        band
        bandage
        bank
        bar
        bargain
        barrier
        base
        based
        base on
        basic
        basically
        basis
        bath
        bathroom
        battery
        battle
        bay
        be
        beach
        beak
        bear
        beard
        beat
        beat up
        beautiful
        beautifully
        beauty
        because
        because of
        become
        bed
        bedroom
        beef
        beer
        before
        begin
        beginning
        behalf
        behave
        behaviour
        behind
        be left over
        belief
        believe
        believe in
        bell
        belong
        belong to
        below
        belt
        bend
        beneath
        benefit
        bent
        beside
        best
        bet
        better
        betting
        between
        beyond
        bicycle
        bid
        big
        bike
        bill
        billion
        bin
        biology
        bird
        birth
        birthday
        biscuit
        bit
        bite
        bitter
        bitterly
        black
        blade
        blame
        blank
        blind
        block
        blonde
        blood
        blow
        blow out
        blow up
        blue
        board
        boat
        body
        boil
        bomb
        bone
        book
        boot
        border
        bore
        bored
        boring
        born
        borrow
        boss
        both
        bother
        bottle
        bottom
        bound
        bowl
        box
        boy
        boyfriend
        brain
        branch
        brand
        brave
        bread
        break
        break down
        breakfast
        break in
        break into
        break off
        break out
        break up
        breast
        breath
        breathe
        breathe in
        breathe out
        breathing
        breed
        brick
        bridge
        brief
        briefly
        bright
        brightly
        brilliant
        bring
        bring back
        bring down
        bring forward
        bring out
        bring up
        broad
        broadcast
        broadly
        broken
        brother
        brown
        brush
        bubble
        budget
        build
        building
        build up
        bullet
        bunch
        burn
        burn down
        burnt
        burst
        burst into
        burst out
        bury
        bus
        bush
        business
        businessman
        busy
        but
        butter
        button
        buy
        buyer
        by
        bye
        cabinet
        cable
        cake
        calculate
        calculation
        call
        call back
        called
        call for
        call off
        call up
        calm
        calm down
        calmly
        camera
        camp
        campaign
        camping
        can
        cancel
        cancer
        candidate
        candy
        cannot
        cap
        capable
        capacity
        capital
        captain
        capture
        car
        card
        cardboard
        care
        career
        care for
        careful
        carefully
        careless
        carelessly
        carpet
        carrot
        carry
        carry on
        carry out
        case
        cash
        cast
        castle
        cat
        catch
        catch up
        category
        cause
        CD
        cease
        ceiling
        celebrate
        celebration
        cell
        cell phone
        cent
        centimetre
        central
        centre
        century
        ceremony
        certain
        certainly
        certificate
        chain
        chair
        chairman
        chairwoman
        challenge
        chamber
        chance
        change
        change round
        channel
        chapter
        character
        characteristic
        charge
        charity
        chart
        chase
        chase away
        chat
        cheap
        cheaply
        cheat
        cheat of
        check
        check in
        check into
        check on
        check out
        check over
        check up on
        cheek
        cheerful
        cheerfully
        cheese
        chemical
        chemist
        chemistry
        cheque
        chest
        chew
        chicken
        chief
        child
        chin
        chip
        chocolate
        choice
        choose
        chop
        chop down
        chop off
        church
        cigarette
        cinema
        circle
        circumstance
        citizen
        city
        civil
        claim
        clap
        class
        classic
        classroom
        clean
        clean up
        clear
        clearly
        clear out
        clear up
        clerk
        clever
        click
        client
        climate
        climb
        climbing
        clock
        close
        closed
        closely
        closet
        cloth
        clothes
        clothing
        cloud
        club
        coach
        coal
        coast
        coat
        code
        coffee
        coin
        cold
        coldly
        collapse
        colleague
        collect
        collection
        college
        colour
        coloured
        column
        combination
        combine
        come
        come across
        come down
        comedy
        come from
        come in
        come off
        come on
        come out
        come round
        come to
        come up
        come up to
        comfort
        comfortable
        comfortably
        command
        comment
        commercial
        commission
        commit
        commitment
        committee
        common
        commonly
        communicate
        communication
        community
        company
        compare
        comparison
        compete
        competition
        competitive
        complain
        complaint
        complete
        completely
        complex
        complicate
        complicated
        computer
        concentrate
        concentrate on
        concentration
        concept
        concern
        concerned
        concerning
        concert
        conclude
        conclusion
        concrete
        condition
        conduct
        conference
        confidence
        confident
        confidently
        confine
        confined
        confirm
        conflict
        confront
        confuse
        confused
        confusing
        confusion
        congratulate
        congratulation
        congress
        connect
        connected
        connection
        conscious
        consequence
        conservative
        consider
        considerable
        considerably
        consideration
        consist
        consist of
        constant
        constantly
        construct
        construction
        consult
        consumer
        contact
        contain
        container
        contemporary
        content
        contest
        context
        continent
        continue
        continuous
        continuously
        contract
        contrast
        contrasting
        contribute
        contribution
        control
        controlled
        convenient
        convention
        conventional
        conversation
        convert
        convince
        cook
        cooker
        cookie
        cooking
        cool
        cool down
        cope
        copy
        core
        corner
        correct
        correctly
        cost
        cottage
        cotton
        cough
        coughing
        could
        council
        count
        counter
        count on
        country
        countryside
        county
        couple
        courage
        course
        court
        cousin
        cover
        covered
        covering
        cover up
        cow
        crack
        cracked
        craft
        crash
        crazy
        cream
        create
        creature
        credit
        credit card
        crime
        criminal
        crisis
        crisp
        criterion
        critical
        criticism
        criticize
        crop
        cross
        cross out
        crowd
        crowded
        crown
        crucial
        cruel
        crush
        cry
        cry out
        cultural
        culture
        cup
        cupboard
        curb
        cure
        curious
        curiously
        curl
        curl up
        curly
        current
        currently
        curtain
        curve
        curved
        custom
        customer
        customs
        cut
        cut back
        cut down
        cut off
        cut out
        cut up
        cycle
        cycling
        dad
        daily
        damage
        damp
        dance
        dancer
        dancing
        danger
        dangerous
        dare
        dark
        data
        date
        date back
        daughter
        day
        dead
        deaf
        deal
        deal in
        deal with
        dear
        death
        debate
        debt
        decade
        decay
        December
        decide
        decide on
        decision
        declare
        decline
        decorate
        decoration
        decorative
        decrease
        deep
        deeply
        defeat
        defence
        defend
        define
        definite
        definitely
        definition
        degree
        delay
        deliberate
        deliberately
        delicate
        delight
        delighted
        deliver
        delivery
        demand
        demonstrate
        dentist
        deny
        department
        departure
        depend
        depend on
        deposit
        depress
        depressed
        depressing
        depth
        derive
        derive from
        describe
        description
        desert
        deserted
        deserve
        design
        desire
        desk
        desperate
        desperately
        despite
        destroy
        destruction
        detail
        detailed
        determination
        determine
        determined
        develop
        development
        device
        devote
        devoted
        devote to
        diagram
        diamond
        diary
        dictionary
        die
        die away
        die out
        diet
        difference
        different
        differently
        difficult
        difficulty
        dig
        digital
        dinner
        direct
        direction
        directly
        director
        dirt
        dirty
        disabled
        disadvantage
        disagree
        disagreement
        disagree with doing
        disappear
        disappoint
        disappointed
        disappointing
        disappointment
        disapproval
        disapprove
        disapproving
        disaster
        disc
        discipline
        discount
        discover
        discovery
        discuss
        discussion
        disease
        disgust
        disgusted
        disgusting
        dish
        dishonest
        dishonestly
        disk
        dislike
        dismiss
        display
        dissolve
        distance
        distinguish
        distribute
        distribution
        district
        disturb
        disturbing
        divide
        division
        divorce
        divorced
        do
        doctor
        document
        dog
        dollar
        domestic
        dominate
        door
        dot
        double
        doubt
        do up
        do with
        do without
        down
        downstairs
        downward
        downwards
        dozen
        draft
        drag
        drama
        dramatic
        dramatically
        draw
        drawer
        drawing
        dream
        dress
        dressed
        dress up
        drink
        drive
        drive away
        drive off
        driver
        driving
        drop
        drop out
        drug
        drugstore
        drum
        drunk
        dry
        dry off
        dry up
        due
        dull
        dump
        during
        dust
        duty
        DVD
        dying
        each
        each other
        ear
        early
        earn
        earth
        ease
        easily
        east
        eastern
        easy
        eat
        eat out
        eat up
        economic
        economy
        edge
        edition
        editor
        educate
        educated
        education
        effect
        effective
        effectively
        efficient
        efficiently
        effort
        e.g.
        egg
        eight
        eighteen
        eighteenth
        eighth
        eightieth
        eighty
        either
        elbow
        elderly
        elect
        election
        electric
        electrical
        electricity
        electronic
        elegant
        element
        elevator
        eleven
        eleventh
        else
        elsewhere
        email
        embarrass
        embarrassed
        embarrassing
        embarrassment
        emerge
        emergency
        emotion
        emotional
        emotionally
        emphasis
        emphasize
        empire
        employ
        employee
        employer
        employment
        empty
        enable
        encounter
        encourage
        encouragement
        end
        end in
        ending
        end up
        enemy
        energy
        engage
        engaged
        engine
        engineer
        engineering
        enjoy
        enjoyable
        enjoyment
        enormous
        enough
        enquiry
        ensure
        enter
        entertain
        entertainer
        entertaining
        entertainment
        enthusiasm
        enthusiastic
        enthusiastically
        entire
        entirely
        entitle
        entrance
        entry
        envelope
        environment
        environmental
        equal
        equally
        equipment
        equivalent
        error
        escape
        especially
        essay
        essential
        essentially
        establish
        estate
        estimate
        etc.
        euro
        even
        evening
        event
        eventually
        ever
        every
        everybody
        everyone
        everything
        everywhere
        evidence
        evil
        ex-
        exact
        exactly
        exaggerate
        exaggerated
        exam
        examination
        examine
        example
        excellent
        except
        exception
        exchange
        excite
        excited
        excitement
        exciting
        exclude
        excluding
        excuse
        executive
        exercise
        exhibit
        exhibition
        exist
        existence
        exit
        expand
        expect
        expectation
        expected
        expense
        expensive
        experience
        experienced
        experiment
        expert
        explain
        explanation
        explode
        explore
        explosion
        export
        expose
        express
        expression
        extend
        extension
        extensive
        extent
        extra
        extraordinary
        extreme
        extremely
        eye
        face
        face up to
        facility
        fact
        factor
        factory
        fail
        failure
        faint
        faintly
        fair
        fairly
        faith
        faithful
        faithfully
        fall
        false
        fame
        familiar
        family
        famous
        fan
        fancy
        far
        farm
        farmer
        farming
        farther
        farthest
        fashion
        fashionable
        fast
        fasten
        fat
        father
        faucet
        fault
        favour
        favourite
        fear
        feather
        feature
        February
        federal
        fee
        feed
        feel
        feeling
        fellow
        female
        fence
        festival
        fetch
        fever
        few
        field
        fifteen
        fifteenth
        fifth
        fiftieth
        fifty
        fight
        fighting
        figure
        figure out
        file
        fill
        fill in
        fill out
        fill up
        film
        final
        finally
        finance
        financial
        find
        find out
        fine
        finely
        finger
        finish
        finished
        finish off
        fire
        firm
        firmly
        first
        fish
        fishing
        fit
        fit in
        five
        fix
        fixed
        flag
        flame
        flash
        flat
        flavour
        flesh
        flight
        float
        flood
        flooded
        flooding
        floor
        flour
        flow
        flower
        flu
        fly
        flying
        focus
        fold
        folding
        follow
        following
        follow up
        food
        foot
        football
        for
        force
        forecast
        foreign
        forest
        forever
        forget
        forgive
        fork
        form
        formal
        formally
        former
        formerly
        formula
        fortieth
        fortune
        forty
        forward
        found
        foundation
        four
        fourteen
        fourteenth
        fourth
        frame
        free
        freedom
        freely
        freeze
        frequent
        frequently
        fresh
        freshly
        Friday
        fridge
        friend
        friendly
        friendship
        frighten
        frighten away/off
        frightened
        frightening
        from
        front
        frozen
        fruit
        fry
        fuel
        full
        fully
        fun
        function
        function as
        fund
        fundamental
        funeral
        funny
        fur
        furniture
        further
        future
        gain
        gallon
        gamble
        gambling
        game
        gap
        garage
        garbage
        garden
        gas
        gasoline
        gate
        gather
        gear
        general
        generally
        generate
        generation
        generous
        generously
        gentle
        gentleman
        gently
        genuine
        genuinely
        geography
        get
        get away
        get away with
        get back
        get by
        get in
        get into
        get off
        get on
        get on with
        get out of
        get over
        get round
        get round to
        get through
        get up
        giant
        gift
        girl
        girlfriend
        give
        give away
        give back
        give in
        give off
        give out
        give up
        glad
        glass
        global
        glove
        glue
        go
        go ahead
        goal
        go away
        go back
        go back to
        go by
        god
        go down
        go into
        gold
        good
        goodbye
        goods
        go off
        go on
        go on doing
        go out
        go out with
        go over
        go round
        go through
        go through with
        go to
        go up
        govern
        government
        governor
        go with
        go without
        grab
        grade
        gradual
        gradually
        grain
        gram
        grammar
        grand
        grandchild
        granddaughter
        grandfather
        grandmother
        grandparent
        grandson
        grant
        grass
        grateful
        grave
        gravely
        great
        greatly
        green
        grey
        grocery
        ground
        group
        grow
        growth
        grow up
        guarantee
        guard
        guess
        guest
        guide
        guilty
        gun
        guy
        habit
        hair
        hairdresser
        half
        hall
        hammer
        hand
        hand back
        hand down
        hand in
        handle
        hand out
        hand over
        hand round
        hang
        hang about
        hang about with
        hang around
        hang around with
        hang on
        hang on to
        hang up
        happen
        happen to
        happily
        happiness
        happy
        hard
        hardly
        harm
        harmful
        harmless
        hat
        hate
        hatred
        have
        have back
        have on
        have to
        he
        head
        headache
        heal
        health
        healthy
        hear
        hear from
        hearing
        hear of
        heart
        heat
        heating
        heat up
        heaven
        heavily
        heavy
        heel
        height
        hell
        hello
        help
        helpful
        help out
        hence
        her
        here
        hero
        hers
        herself
        hesitate
        hi
        hide
        high
        highlight
        highly
        highway
        hill
        him
        himself
        hip
        hire
        hire out
        his
        historical
        history
        hit
        hobby
        hold
        hold back
        hold on
        hold on to
        hold out
        hold up
        hole
        holiday
        hollow
        holy
        home
        homework
        honest
        honestly
        honour
        hook
        hope
        horizontal
        horn
        horror
        horse
        hospital
        host
        hot
        hotel
        hour
        house
        household
        housing
        how
        however
        huge
        human
        humorous
        humour
        hundred
        hundredth
        hungry
        hunt
        hunting
        hurry
        hurry up
        hurt
        husband
        I
        ice
        ice cream
        idea
        ideal
        identify
        identify with
        identity
        i.e.
        if
        ignore
        ill
        illegal
        illegally
        illness
        illustrate
        image
        imaginary
        imagination
        imagine
        immediate
        immediately
        immoral
        impact
        impatient
        implication
        imply
        import
        importance
        important
        importantly
        impose
        impossible
        impress
        impressed
        impression
        impressive
        improve
        improvement
        in
        inability
        inch
        incident
        include
        including
        income
        increase
        increasingly
        indeed
        independence
        independent
        independently
        index
        indicate
        indication
        indirect
        indirectly
        individual
        indoor
        indoors
        industrial
        industry
        inevitable
        inevitably
        infect
        infected
        infection
        infectious
        influence
        inform
        informal
        information
        ingredient
        initial
        initially
        initiative
        injure
        injured
        injury
        ink
        inner
        innocent
        insect
        insert
        inside
        insist
        insist on
        insist on doing
        install
        instance
        instead
        instead of
        institute
        institution
        instruction
        instrument
        insult
        insulting
        insurance
        intelligence
        intelligent
        intend
        intended
        intention
        interest
        interested
        interesting
        interior
        internal
        international
        Internet
        interpret
        interpretation
        interrupt
        interruption
        interval
        interview
        into
        introduce
        introduction
        invent
        invention
        invest
        investigate
        investigation
        investment
        invitation
        invite
        involve
        involved
        involvement
        iron
        irritate
        irritated
        irritating
        island
        issue
        it
        item
        its
        itself
        jacket
        jam
        January
        jealous
        jeans
        jelly
        jewellery
        job
        join
        join in
        joint
        jointly
        joke
        journalist
        journey
        joy
        judge
        judgement
        juice
        July
        jump
        June
        junior
        just
        justice
        justified
        justify
        keen
        keep
        keep out
        keep out of
        keep up
        keep up with
        key
        keyboard
        kick
        kid
        kill
        killing
        kilogram
        kilometre
        kind
        kindly
        kindness
        king
        kiss
        kitchen
        knee
        knife
        knit
        knitted
        knitting
        knock
        knock down
        knock out
        knot
        know
        knowledge
        lab
        label
        laboratory
        labour
        lack
        lacking
        lady
        lake
        lamp
        land
        landscape
        lane
        language
        large
        largely
        last
        late
        later
        latest
        latter
        laugh
        laugh at
        launch
        law
        lawyer
        lay
        layer
        lazy
        lead
        leader
        leading
        leaf
        league
        lean
        learn
        least
        leather
        leave
        leave out
        lecture
        left
        leg
        legal
        legally
        lemon
        lend
        length
        less
        lesson
        let
        let down
        let off
        letter
        level
        library
        licence
        license
        lid
        lie
        lie around
        lie down
        life
        lift
        light
        lightly
        like
        likely
        limit
        limited
        limit to
        line
        link
        lip
        liquid
        list
        listen
        literature
        litre
        little
        live
        lively
        live on
        live through
        live together
        living
        load
        loan
        local
        locally
        locate
        located
        location
        lock
        lock up
        logic
        logical
        lonely
        long
        look
        look after
        look at
        look down on
        look forward to
        look into
        look on
        look on with
        look out
        look out for
        look round
        look through
        look up
        look up to
        loose
        loosely
        lord
        lorry
        lose
        loss
        lost
        lot
        loud
        loudly
        love
        lovely
        lover
        low
        loyal
        luck
        lucky
        luggage
        lump
        lunch
        lung
        machine
        machinery
        mad
        magazine
        magic
        mail
        main
        mainly
        maintain
        major
        majority
        make
        make into
        make up
        make-up
        make up for
        male
        mall
        man
        manage
        management
        manager
        manner
        manufacture
        manufacturer
        manufacturing
        many
        map
        march
        March
        mark
        market
        marketing
        marriage
        married
        marry
        mass
        massive
        master
        match
        matching
        match up
        mate
        material
        mathematics
        matter
        maximum
        may
        May
        maybe
        mayor
        me
        meal
        mean
        meaning
        means
        meanwhile
        measure
        measurement
        meat
        media
        medical
        medicine
        medium
        meet
        meeting
        meet up
        meet with
        melt
        member
        membership
        memory
        mental
        mentally
        mention
        menu
        mere
        merely
        mess
        message
        metal
        method
        metre
        mid-
        midday
        middle
        midnight
        might
        mild
        mile
        military
        milk
        milligram
        millimetre
        million
        millionth
        mind
        mine
        mineral
        minimum
        minister
        ministry
        minor
        minority
        minute
        mirror
        miss
        missing
        miss out
        mistake
        mistake for
        mistaken
        mix
        mixed
        mixture
        mix up
        mobile
        mobile phone
        model
        modern
        mom
        moment
        Monday
        money
        monitor
        month
        mood
        moon
        moral
        morally
        more
        moreover
        morning
        most
        mostly
        mother
        motion
        motor
        motorbike
        motorcycle
        mount
        mountain
        mouse
        mouth
        move
        move in
        movement
        move out
        move over
        movie
        movie theater
        moving
        Mr
        Mrs
        Ms
        much
        mud
        multiply
        mum
        murder
        muscle
        museum
        music
        musical
        musician
        must
        my
        myself
        mysterious
        mystery
        nail
        naked
        name
        narrow
        nation
        national
        natural
        naturally
        nature
        navy
        near
        nearby
        nearly
        neat
        neatly
        necessarily
        necessary
        neck
        need
        needle
        negative
        neighbour
        neighbourhood
        neither
        nephew
        nerve
        nervous
        nervously
        nest
        net
        network
        never
        nevertheless
        new
        newly
        news
        newspaper
        next
        next to
        nice
        nicely
        niece
        night
        nine
        nineteen
        nineteenth
        ninetieth
        ninety
        ninth
        no
        nobody
        noise
        noisily
        noisy
        non-
        none
        nonsense
        no one
        nor
        normal
        normally
        north
        northern
        nose
        not
        note
        note down
        nothing
        notice
        noticeable
        novel
        November
        now
        nowhere
        nuclear
        number
        nurse
        nut
        obey
        object
        objective
        observation
        observe
        obtain
        obvious
        obviously
        occasion
        occasionally
        occupied
        occupy
        occur
        occur to
        ocean
        o’clock
        October
        odd
        oddly
        of
        off
        offence
        offend
        offense
        offensive
        offer
        office
        officer
        official
        officially
        often
        oh
        oil
        OK
        old
        old-fashioned
        on
        once
        one
        one another
        onion
        online
        only
        onto
        open
        opening
        openly
        open up
        operate
        operation
        opinion
        opponent
        opportunity
        oppose
        opposed
        opposing
        opposite
        opposition
        option
        or
        orange
        order
        ordinary
        organ
        organization
        organize
        organized
        origin
        original
        originally
        other
        otherwise
        ought to
        our
        ours
        ourselves
        out
        outdoor
        outdoors
        outer
        outline
        output
        outside
        outstanding
        oven
        over
        overall
        overcome
        owe
        own
        owner
        own up
        pace
        pack
        package
        packaging
        packet
        pack up
        page
        pain
        painful
        paint
        painter
        painting
        pair
        palace
        pale
        pan
        panel
        pants
        paper
        parallel
        parent
        park
        parliament
        part
        particular
        particularly
        partly
        partner
        partnership
        party
        pass
        passage
        pass away
        pass by
        passenger
        passing
        pass on
        pass out
        passport
        pass round
        pass through
        past
        path
        patience
        patient
        pattern
        pause
        pay
        pay back
        payment
        pay out
        pay up
        peace
        peaceful
        peak
        pen
        pencil
        penny
        pension
        people
        pepper
        per
        per cent
        perfect
        perfectly
        perform
        performance
        performer
        perhaps
        period
        permanent
        permanently
        permission
        permit
        person
        personal
        personality
        personally
        persuade
        pet
        petrol
        phase
        philosophy
        phone
        photo
        photocopy
        photograph
        photographer
        photography
        phrase
        physical
        physically
        physics
        piano
        pick
        pick up
        picture
        piece
        pig
        pile
        pile up
        pill
        pilot
        pin
        pink
        pint
        pipe
        pitch
        pity
        place
        plain
        plan
        plane
        planet
        planning
        plant
        plastic
        plate
        platform
        play
        play about
        player
        play with
        pleasant
        pleasantly
        please
        pleased
        pleasing
        pleasure
        plenty
        plot
        plug
        plug in
        plus
        p.m.
        pocket
        poem
        poetry
        point
        pointed
        point out
        poison
        poisonous
        pole
        police
        policy
        polish
        polite
        politely
        political
        politically
        politician
        politics
        pollution
        pool
        poor
        pop
        popular
        population
        port
        pose
        position
        positive
        possess
        possession
        possibility
        possible
        possibly
        post
        post office
        pot
        potato
        potential
        potentially
        pound
        pour
        powder
        power
        powerful
        practical
        practically
        practice
        practise
        praise
        pray
        prayer
        precise
        precisely
        predict
        prefer
        preference
        pregnant
        premises
        preparation
        prepare
        prepared
        presence
        present
        presentation
        preserve
        president
        press
        pressure
        presumably
        pretend
        pretty
        prevent
        previous
        previously
        price
        pride
        priest
        primarily
        primary
        prime minister
        prince
        princess
        principle
        print
        printer
        printing
        print off
        prior
        priority
        prison
        prisoner
        private
        privately
        prize
        probable
        probably
        problem
        procedure
        proceed
        process
        produce
        producer
        product
        production
        profession
        professional
        professor
        profit
        program
        programme
        progress
        project
        promise
        promote
        promotion
        prompt
        promptly
        pronounce
        pronunciation
        proof
        proper
        properly
        property
        proportion
        proposal
        propose
        prospect
        protect
        protection
        protest
        proud
        proudly
        prove
        provide
        provided
        providing
        pub
        public
        publication
        publicity
        publicly
        publish
        publishing
        pull
        pull apart
        pull down
        pull in
        pull off
        pull out
        pull over
        pull through
        pull together
        pull up
        punch
        punish
        punishment
        pupil
        purchase
        pure
        purely
        purple
        purpose
        pursue
        push
        push about
        push forward
        put
        put away
        put back
        put down
        put forward
        put in
        put off
        put on
        put out
        put through
        put together
        put up
        put up with
        qualification
        qualified
        qualify
        quality
        quantity
        quarter
        queen
        question
        quick
        quickly
        quiet
        quietly
        quit
        quite
        quote
        race
        racing
        radio
        rail
        railroad
        railway
        rain
        raise
        range
        rank
        rapid
        rapidly
        rare
        rarely
        rate
        rather
        raw
        re-
        reach
        react
        reaction
        read
        reader
        reading
        read out
        read over
        ready
        real
        realistic
        reality
        realize
        really
        rear
        reason
        reasonable
        reasonably
        recall
        receipt
        receive
        recent
        recently
        reception
        reckon
        reckon on
        recognition
        recognize
        recommend
        record
        recording
        recover
        red
        reduce
        reduction
        refer
        reference
        refer to
        reflect
        reform
        refrigerator
        refusal
        refuse
        regard
        regarding
        region
        regional
        register
        regret
        regular
        regularly
        regulation
        reject
        relate
        related
        relate to
        relation
        relationship
        relative
        relatively
        relax
        relaxed
        relaxing
        release
        relevant
        relief
        religion
        religious
        rely
        rely on
        remain
        remaining
        remains
        remark
        remarkable
        remarkably
        remember
        remind
        remind of
        remote
        removal
        remove
        rent
        rented
        repair
        repeat
        repeated
        repeatedly
        replace
        reply
        report
        represent
        representative
        reproduce
        reputation
        request
        require
        requirement
        rescue
        research
        reservation
        reserve
        resident
        resist
        resistance
        resolve
        resort
        resort to
        resource
        respect
        respond
        response
        responsibility
        responsible
        rest
        restaurant
        restore
        restrict
        restricted
        restriction
        result
        result in
        retain
        retire
        retired
        retirement
        return
        reveal
        reverse
        review
        revise
        revision
        revolution
        reward
        rhythm
        rice
        rich
        rid
        ride
        rider
        ridiculous
        riding
        right
        rightly
        ring
        ring 2
        ring back
        rise
        risk
        rival
        river
        road
        rob
        rock
        role
        roll
        romantic
        roof
        room
        root
        rope
        rough
        roughly
        round
        rounded
        route
        routine
        row
        royal
        rub
        rubber
        rubbish
        rude
        rudely
        ruin
        ruined
        rule
        rule out
        ruler
        rumour
        run
        run after
        run away
        runner
        running
        run out
        run over
        run through
        rural
        rush
        sack
        sad
        sadly
        sadness
        safe
        safely
        safety
        sail
        sailing
        sailor
        salad
        salary
        sale
        salt
        salty
        same
        sample
        sand
        satisfaction
        satisfied
        satisfy
        satisfying
        Saturday
        sauce
        save
        saving
        say
        scale
        scare
        scared
        scare off
        scene
        schedule
        scheme
        school
        science
        scientific
        scientist
        scissors
        score
        scratch
        scream
        screen
        screw
        sea
        seal
        seal off
        search
        season
        seat
        second
        secondary
        secret
        secretary
        secretly
        section
        sector
        secure
        security
        see
        see about
        seed
        seek
        seem
        see to
        select
        selection
        self
        self-
        sell
        sell off
        sell out
        senate
        senator
        send
        send for
        send off
        senior
        sense
        sensible
        sensitive
        sentence
        separate
        separated
        separately
        separation
        September
        series
        serious
        seriously
        servant
        serve
        service
        session
        set
        set off
        set out
        settle
        settle down
        set up
        seven
        seventeen
        seventh
        seventieth
        seventy
        several
        severe
        severely
        sew
        sewing
        sex
        sexual
        sexually
        shade
        shadow
        shake
        shall
        shallow
        shame
        shape
        shaped
        share
        sharp
        sharply
        shave
        she
        sheep
        sheet
        shelf
        shell
        shelter
        shift
        shine
        shiny
        ship
        shirt
        shock
        shocked
        shocking
        shoe
        shoot
        shoot down
        shooting
        shop
        shopping
        short
        shortly
        shot
        should
        shoulder
        shout
        show
        shower
        show off
        show round
        show up
        shut
        shut down
        shut in
        shut out
        shut up
        shy
        sick
        side
        sideways
        sight
        sign
        signal
        signature
        significant
        significantly
        silence
        silent
        silk
        silly
        silver
        similar
        similarly
        simple
        simply
        since
        sincere
        sincerely
        sing
        singer
        singing
        single
        sink
        sir
        sister
        sit
        sit down
        site
        situation
        six
        sixteen
        sixth
        sixtieth
        sixty
        size
        skilful
        skilfully
        skill
        skilled
        skin
        skirt
        sky
        sleep
        sleeve
        slice
        slide
        slight
        slightly
        slip
        slope
        slow
        slowly
        small
        smart
        smash
        smell
        smile
        smoke
        smoking
        smooth
        smoothly
        snake
        snow
        so
        soap
        social
        socially
        society
        sock
        soft
        softly
        software
        soil
        soldier
        solid
        solution
        solve
        some
        somebody
        somehow
        someone
        something
        sometimes
        somewhat
        somewhere
        son
        song
        soon
        sore
        sorry
        sort
        sort out
        soul
        sound
        soup
        sour
        source
        south
        southern
        space
        spare
        speak
        speaker
        speak out
        speak up
        special
        specialist
        specially
        specific
        specifically
        speech
        speed
        speed up
        spell
        spelling
        spend
        spice
        spicy
        spider
        spin
        spirit
        spiritual
        spite
        split
        split up
        spoil
        spoken
        spoon
        sport
        spot
        spray
        spread
        spread out
        spring
        square
        squeeze
        stable
        staff
        stage
        stair
        stamp
        stand
        standard
        stand back
        stand by
        stand for
        stand out
        stand up
        stand up for
        star
        stare
        start
        start off
        start out
        start up
        state
        statement
        station
        statue
        status
        stay
        stay away
        stay out of
        steadily
        steady
        steal
        steam
        steel
        steep
        steeply
        steer
        step
        stick
        stick out
        stick to
        stick up
        sticky
        stiff
        stiffly
        still
        sting
        stir
        stock
        stomach
        stone
        stop
        store
        storm
        story
        stove
        straight
        strain
        strange
        strangely
        stranger
        strategy
        stream
        street
        strength
        stress
        stressed
        stretch
        strict
        strictly
        strike
        striking
        string
        strip
        stripe
        striped
        stroke
        strong
        strongly
        structure
        struggle
        student
        studio
        study
        stuff
        stupid
        style
        subject
        substance
        substantial
        substantially
        substitute
        succeed
        success
        successful
        successfully
        such
        suck
        sudden
        suddenly
        suffer
        suffering
        sufficient
        sufficiently
        sugar
        suggest
        suggestion
        suit
        suitable
        suitcase
        suited
        sum
        summary
        summer
        sum up
        sun
        Sunday
        superior
        supermarket
        supply
        support
        supporter
        suppose
        sure
        surely
        surface
        surname
        surprise
        surprised
        surprising
        surprisingly
        surround
        surrounding
        surroundings
        survey
        survive
        suspect
        suspicion
        suspicious
        swallow
        swear
        swearing
        sweat
        sweater
        sweep
        sweet
        swell
        swelling
        swim
        swimming
        swimming pool
        swing
        switch
        switch off
        swollen
        symbol
        sympathetic
        sympathy
        system
        table
        tablet
        tackle
        tail
        take
        take away
        take back
        take down
        take in
        take off
        take on
        take over
        take up
        talk
        tall
        tank
        tap
        tape
        target
        task
        taste
        tax
        taxi
        tea
        teach
        teacher
        teaching
        team
        tear
        tear 2
        tear up
        technical
        technique
        technology
        telephone
        television
        tell
        tell off
        temperature
        temporarily
        temporary
        ten
        tend
        tendency
        tension
        tent
        tenth
        term
        terrible
        terribly
        test
        text
        than
        thank
        thanks
        thank you
        that
        the
        theatre
        their
        theirs
        them
        theme
        themselves
        then
        theory
        there
        therefore
        they
        thick
        thickly
        thickness
        thief
        thin
        thing
        think
        think about
        thinking
        think of
        think of as
        think over
        think up
        third
        thirsty
        thirteen
        thirteenth
        thirtieth
        thirty
        this
        thorough
        thoroughly
        though
        thought
        thousand
        thousandth
        thread
        threat
        threaten
        threatening
        three
        throat
        through
        throughout
        throw
        throw away
        throw out
        thumb
        Thursday
        thus
        ticket
        tidy
        tie
        tie up
        tight
        tightly
        till
        time
        timetable
        tin
        tiny
        tip
        tip over
        tire
        tired
        tire out
        tiring
        title
        to
        today
        toe
        together
        toilet
        tomato
        tomorrow
        ton
        tone
        tongue
        tonight
        tonne
        too
        tool
        tooth
        top
        topic
        total
        totally
        touch
        tough
        tour
        tourist
        towards
        towel
        tower
        town
        toy
        trace
        track
        trade
        trading
        tradition
        traditional
        traditionally
        traffic
        train
        training
        transfer
        transform
        translate
        translation
        transparent
        transport
        transportation
        trap
        travel
        traveller
        treat
        treatment
        tree
        trend
        trial
        triangle
        trick
        trip
        tropical
        trouble
        trousers
        truck
        true
        truly
        trust
        truth
        try
        try on
        try out
        tube
        Tuesday
        tune
        tunnel
        turn
        turn back
        turn down
        turn into
        turn off
        turn on
        turn out
        turn over
        turn round
        turn to
        turn up
        TV
        twelfth
        twelve
        twentieth
        twenty
        twice
        twin
        twist
        twisted
        two
        type
        typical
        typically
        tyre
        the unemployed
        the unexpected
        ugly
        ultimate
        ultimately
        umbrella
        unable
        unacceptable
        uncertain
        uncle
        uncomfortable
        unconscious
        uncontrolled
        under
        underground
        underneath
        understand
        understanding
        underwater
        underwear
        undo
        unemployed
        unemployment
        unexpected
        unexpectedly
        unfair
        unfairly
        unfortunate
        unfortunately
        unfriendly
        unhappy
        uniform
        unimportant
        union
        unique
        unit
        unite
        united
        universe
        university
        unkind
        unknown
        unless
        unlike
        unlikely
        unload
        unlucky
        unnecessary
        unpleasant
        unreasonable
        unsteady
        unsuccessful
        untidy
        until
        unusual
        unusually
        unwilling
        unwillingly
        up
        upon
        upper
        upset
        upsetting
        upside down
        upstairs
        upward
        upwards
        urban
        urge
        urgent
        us
        use
        used
        used 2
        used to
        useful
        useless
        user
        use up
        usual
        usually
        vacation
        valid
        valley
        valuable
        value
        van
        variation
        varied
        variety
        various
        vary
        vast
        vegetable
        vehicle
        venture
        version
        vertical
        very
        via
        victim
        victory
        video
        view
        village
        violence
        violent
        violently
        virtually
        virus
        visible
        vision
        visit
        visitor
        vital
        vocabulary
        voice
        volume
        vote
        wage
        waist
        wait
        waiter
        wake
        walk
        walking
        walk out
        walk up
        wall
        wallet
        wander
        want
        war
        warm
        warmth
        warm up
        warn
        warning
        wash
        wash away
        washing
        wash off
        wash out
        wash up
        waste
        watch
        watch out
        watch out for
        water
        wave
        way
        we
        weak
        weakness
        wealth
        weapon
        wear
        wear away
        wear off
        wear out
        weather
        web
        website
        wedding
        Wednesday
        week
        weekend
        weekly
        weigh
        weight
        welcome
        well
        well known
        west
        western
        wet
        what
        whatever
        wheel
        when
        whenever
        where
        whereas
        wherever
        whether
        which
        while
        whisper
        whistle
        white
        who
        whoever
        whole
        whom
        whose
        why
        wide
        widely
        width
        wife
        wild
        wildly
        will
        willing
        willingly
        willingness
        win
        wind
        wind 2
        window
        wine
        wing
        winner
        winning
        winter
        wire
        wise
        wish
        with
        withdraw
        within
        without
        witness
        woman
        wonder
        wonderful
        wood
        wooden
        wool
        word
        work
        worker
        working
        work out
        world
        worried
        worry
        worrying
        worse
        worship
        worst
        worth
        would
        wound
        wounded
        wrap
        wrapping
        wrist
        write
        write back
        write down
        writer
        writing
        written
        wrong
        wrongly
        yard
        yawn
        yeah
        year
        yellow
        yes
        yesterday
        yet
        you
        young
        your
        yours
        yourself
        youth
        zero
        zone
    """.trimIndent().split("\n")
}